package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.*;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.repository.TraineeHibernateRepository;
import org.example.repository.TrainerHibernateRepository;
import org.example.repository.TrainingHibernateRepository;
import org.example.repository.TrainingPartnershipHibernateRepository;
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
    private TrainerHibernateRepository trainerHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TraineeRepository traineeRepository;
    @Autowired
    private TraineeHibernateRepository traineeHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingRepository trainingRepository;
    @Autowired
    private TrainingHibernateRepository trainingHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingPartnershipRepository trainingPartnershipRepository;
    @Autowired
    private TrainingPartnershipHibernateRepository trainingPartnershipHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public boolean create(String authUsername, CreateTrainingForTraineeRequest request) {
        TrainingPartnership partnership = trainingPartnershipHibernateRepository.findPartnershipByTraineeTrainerUsernames(authUsername, request.getTrainerUsername()).orElseThrow(()->new NoSuchEntityException("training partnership not found"));

        if (partnership.getTrainee().getUser().isRemoved())
            throw new RemovedEntityException("trainee has been removed");
        if (!partnership.getTrainer().getUser().isActive())
            throw new RemovedEntityException("trainer is inactive");
        if (!partnership.getTrainee().getUser().isActive())
            throw new RemovedEntityException("trainee is inactive");
        if (partnership.isRemoved())
            throw new RemovedEntityException("training partnership has been removed");

        Training training = Training.builder()
                .trainingName(request.getName())
                .isRemoved(false)
                .trainingPartnership(partnership)
                .trainingDateFrom(request.getDate())
                .trainingDateTo(Timestamp.from(request.getDate().toInstant().plus(request.getDuration())))
                .build();

        trainingHibernateRepository.saveAndFlush(training);
        return true;
    }
    @Override
    public boolean create(String authUsername, CreateTrainingForTrainerRequest request) {
        CreateTrainingForTraineeRequest req = CreateTrainingForTraineeRequest.builder()
                .date(request.getDate())
                .duration(request.getDuration())
                .name(request.getName())
                .trainerUsername(authUsername)
                .build();
        return create(request.getTraineeUsername(), req);
    }


    @Override
    public MultipleTrainingInfoResponse getByTrainee(String authUsername, GetTraineeTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        filter.setTraineeName(authUsername);
        if (filter.getTraineeName() == null)
            throw new BadRequestException("requires trainee username");
        Optional<Trainee> traineeOptional = traineeHibernateRepository.findTraineeByUsername(filter.getTraineeName());
        if (traineeOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (traineeOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");


        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingHibernateRepository.findTrainingByFilter(filter).stream()
                        .filter(t->!t.getTrainingPartnership().getTrainer().isRemoved()) //not needed because trainers can't be removed
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public MultipleTrainingInfoResponse getByTrainer(String authUsername, GetTrainerTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        filter.setTrainerName(authUsername);
        if (filter.getTrainerName() == null)
            throw new BadRequestException("requires trainer username");
        Optional<Trainer> trainerOptional = trainerHibernateRepository.findTrainerByUsername(filter.getTrainerName());
        if (trainerOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (trainerOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");

        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingHibernateRepository.findTrainingByFilter(filter).stream()
                        .filter(t->!t.getTrainingPartnership().getTrainee().isRemoved()) //don't show trainings from removed trainees
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
