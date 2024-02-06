package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.*;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.repository.StudentHibernateRepository;
import org.example.repository.MentorHibernateRepository;
import org.example.repository.TrainingHibernateRepository;
import org.example.repository.PartnershipHibernateRepository;
import org.example.requests_responses.training.*;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

    //@Setter(onMethod_={@Autowired})
    //private TrainerRepository trainerRepository;
    @Autowired
    private MentorHibernateRepository mentorHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TraineeRepository traineeRepository;
    @Autowired
    private StudentHibernateRepository studentHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingRepository trainingRepository;
    @Autowired
    private TrainingHibernateRepository trainingHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingPartnershipRepository trainingPartnershipRepository;
    @Autowired
    private PartnershipHibernateRepository partnershipHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public boolean create(String authUsername, CreateTrainingForStudentRequest request) {
        Partnership partnership = partnershipHibernateRepository.findPartnershipByTraineeTrainerUsernames(authUsername, request.getMentorUsername()).orElseThrow(()->new NoSuchEntityException("training partnership not found"));

        if (partnership.getStudent().getUser().isRemoved())
            throw new RemovedEntityException("trainee has been removed");
        if (!partnership.getMentor().getUser().isActive())
            throw new RemovedEntityException("trainer is inactive");
        if (!partnership.getStudent().getUser().isActive())
            throw new RemovedEntityException("trainee is inactive");
        if (partnership.isRemoved())
            throw new RemovedEntityException("training partnership has been removed");

        Training training = Training.builder()
                .trainingName(request.getName())
                .isRemoved(false)
                .partnership(partnership)
                .trainingDateFrom(request.getDate())
                .trainingDateTo(Timestamp.from(request.getDate().toInstant().plus(request.getDuration())))
                .build();

        trainingHibernateRepository.saveAndFlush(training);
        return true;
    }
    @Override
    public boolean create(String authUsername, CreateTrainingForMentorRequest request) {
        CreateTrainingForStudentRequest req = CreateTrainingForStudentRequest.builder()
                .date(request.getDate())
                .duration(request.getDuration())
                .name(request.getName())
                .mentorUsername(authUsername)
                .build();
        return create(request.getStudentUsername(), req);
    }


    @Override
    public MultipleTrainingInfoResponse getByStudent(String authUsername, GetStudentTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        filter.setStudentName(authUsername);
        if (filter.getStudentName() == null)
            throw new BadRequestException("requires trainee username");
        Optional<Student> traineeOptional = studentHibernateRepository.findStudentByUsername(filter.getStudentName());
        if (traineeOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (traineeOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");


        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingHibernateRepository.findTrainingByFilter(filter).stream()
                        .filter(t->!t.getPartnership().getMentor().isRemoved()) //not needed because trainers can't be removed
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public MultipleTrainingInfoResponse getByMentor(String authUsername, GetMentorTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        filter.setMentorName(authUsername);
        if (filter.getMentorName() == null)
            throw new BadRequestException("requires trainer username");
        Optional<Mentor> trainerOptional = mentorHibernateRepository.findMentorByUsername(filter.getMentorName());
        if (trainerOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (trainerOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");

        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingHibernateRepository.findTrainingByFilter(filter).stream()
                        .filter(t->!t.getPartnership().getStudent().isRemoved()) //don't show trainings from removed trainees
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
