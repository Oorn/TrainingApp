package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Student;
import org.example.domain_entities.Mentor;
import org.example.domain_entities.Partnership;
import org.example.exceptions.NoPermissionException;
import org.example.exceptions.NoSuchEntityException;
import org.example.repository.StudentHibernateRepository;
import org.example.repository.MentorHibernateRepository;
import org.example.repository.PartnershipHibernateRepository;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.service.PartnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PartnershipServiceImpl implements PartnershipService {

    //@Setter(onMethod_={@Autowired})
    //private TrainerRepository trainerRepository;
    @Autowired
    private MentorHibernateRepository mentorHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TraineeRepository traineeRepository;
    @Autowired
    private StudentHibernateRepository studentHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingPartnershipRepository trainingPartnershipRepository;
    @Autowired
    private PartnershipHibernateRepository partnershipHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public AvailableTrainersResponse getNotAssignedMentors(String traineeUsername) {
        if (studentHibernateRepository.findStudentByUsername(traineeUsername).isEmpty())
            throw new NoSuchEntityException("trainee not found");
        Set<Mentor> assignedMentors = partnershipHibernateRepository.findPartnershipByTraineeUsername(traineeUsername).stream()
                .filter(t->!t.isRemoved())
                .map(Partnership::getMentor)
                .collect(Collectors.toSet());
        return AvailableTrainersResponse.builder()
                .mentors(mentorHibernateRepository.findAll().stream()
                        .filter(t->!t.isRemoved())
                        .filter(t->t.getUser().isActive())
                        .filter(t->!assignedMentors.contains(t))
                        .map(t->converter.convert(t, MentorShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UpdateTrainingPartnershipListResponse updateStudentMentorsList(String authUsername, UpdateTrainingPartnershipListRequest request) {

        //step 1 - check trainee(present, not removed, active)
        //step 2 - get old partnership list, and calculate differences (create, remove, undo remove)
        //step 3 - validate difference (added trainers exist, and are active).
        //step 4 - apply

        Student student = studentHibernateRepository.findStudentByUsername(authUsername).orElseThrow(()->new NoSuchEntityException("trainee not found"));
        if (student.isRemoved())
            //should be unreachable because of auth
            throw new NoSuchEntityException("trainee has been removed");
        if (!student.getUser().isActive())
            throw new NoPermissionException("trainee is not active");
        //STEP 1 DONE
        List<Partnership> existingPartnerships = partnershipHibernateRepository.findPartnershipByTraineeUsername(authUsername);
        List<Partnership> pendingRemoval = existingPartnerships.stream()
                .filter((tp) -> !tp.isRemoved())
                .filter((tp) -> !request.getMentorUsernames().contains(tp.getMentor().getUser().getUserName()))
                .collect(Collectors.toList());
        List<Partnership> pendingReAddition = existingPartnerships.stream()
                .filter(Partnership::isRemoved)
                .filter((tp) -> request.getMentorUsernames().contains(tp.getMentor().getUser().getUserName()))
                .collect(Collectors.toList());
        Set<String> existingPartnershipsTrainerNames = existingPartnerships.stream()
                .map(tp -> tp.getMentor().getUser().getUserName())
                .collect(Collectors.toSet());
        Set<String> pendingCreationNames = request.getMentorUsernames().stream()
                .filter(s -> !existingPartnershipsTrainerNames.contains(s))
                .collect(Collectors.toSet());
        //STEP 2 DONE
        List<Mentor> pendingCreationMentors = pendingCreationNames.stream()
                .map(s -> mentorHibernateRepository.findMentorByUsername(s).orElseThrow(() -> new NoSuchEntityException("trainer " + s + " not found")))
                .peek(t -> {
                    if (!t.getUser().isActive()) throw new NoPermissionException("trainer " + t.getUser().getUserName() + " is inactive");
                })
                .collect(Collectors.toList());
        //STEP 3 DONE
        //TODO ADD BATCH SAVE HERE
        pendingRemoval.forEach(tp -> {
                    tp.setRemoved(true);
                    partnershipHibernateRepository.saveAndFlush(tp);
                });
        pendingReAddition.forEach(tp -> {
            tp.setRemoved(false);
            partnershipHibernateRepository.saveAndFlush(tp);
        });
        pendingCreationMentors.stream()
                .map(t -> Partnership.builder()
                        .student(student)
                        .mentor(t)
                        .isRemoved(false)
                        .build())
                .forEach(partnershipHibernateRepository::saveAndFlush);
        //STEP 4 DONE

        return UpdateTrainingPartnershipListResponse.builder()
                .mentorsList(partnershipHibernateRepository.findPartnershipByTraineeUsername(authUsername)
                        .stream()
                        .filter(tp -> !tp.isRemoved())
                        .filter(tp -> tp.getMentor().getUser().isActive())
                        .map(tp -> converter.convert(tp.getMentor(), MentorShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();

    }
}
