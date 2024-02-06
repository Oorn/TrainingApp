package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.exceptions.NoPermissionException;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.impl.v2.hibernate.TraineeHibernateRepository;
import org.example.repository.impl.v2.hibernate.TrainerHibernateRepository;
import org.example.repository.impl.v2.hibernate.TrainingPartnershipHibernateRepository;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.service.TrainingPartnershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrainingPartnershipServiceImpl implements TrainingPartnershipService {

    //@Setter(onMethod_={@Autowired})
    //private TrainerRepository trainerRepository;
    @Autowired
    private TrainerHibernateRepository trainerHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TraineeRepository traineeRepository;
    @Autowired
    private TraineeHibernateRepository traineeHibernateRepository;

    //@Setter(onMethod_={@Autowired})
    //private TrainingPartnershipRepository trainingPartnershipRepository;
    @Autowired
    private TrainingPartnershipHibernateRepository trainingPartnershipHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public AvailableTrainersResponse getNotAssignedTrainers(String traineeUsername) {
        if (traineeHibernateRepository.findTraineeByUsername(traineeUsername).isEmpty())
            throw new NoSuchEntityException("trainee not found");
        Set<Trainer> assignedTrainers = trainingPartnershipHibernateRepository.findPartnershipByTraineeUsername(traineeUsername).stream()
                .filter(t->!t.isRemoved())
                .map(TrainingPartnership::getTrainer)
                .collect(Collectors.toSet());
        return AvailableTrainersResponse.builder()
                .trainers(trainerHibernateRepository.findAll().stream()
                        .filter(t->!t.isRemoved())
                        .filter(t->t.getUser().isActive())
                        .filter(t->!assignedTrainers.contains(t))
                        .map(t->converter.convert(t, TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UpdateTrainingPartnershipListResponse updateTraineeTrainerList(String authUsername, UpdateTrainingPartnershipListRequest request) {

        //step 1 - check trainee(present, not removed, active)
        //step 2 - get old partnership list, and calculate differences (create, remove, undo remove)
        //step 3 - validate difference (added trainers exist, and are active).
        //step 4 - apply

        Trainee trainee = traineeHibernateRepository.findTraineeByUsername(authUsername).orElseThrow(()->new NoSuchEntityException("trainee not found"));
        if (trainee.isRemoved())
            //should be unreachable because of auth
            throw new NoSuchEntityException("trainee has been removed");
        if (!trainee.getUser().isActive())
            throw new NoPermissionException("trainee is not active");
        //STEP 1 DONE
        List<TrainingPartnership> existingPartnerships = trainingPartnershipHibernateRepository.findPartnershipByTraineeUsername(authUsername);
        List<TrainingPartnership> pendingRemoval = existingPartnerships.stream()
                .filter((tp) -> !tp.isRemoved())
                .filter((tp) -> !request.getTrainerUsernames().contains(tp.getTrainer().getUser().getUserName()))
                .collect(Collectors.toList());
        List<TrainingPartnership> pendingReAddition = existingPartnerships.stream()
                .filter(TrainingPartnership::isRemoved)
                .filter((tp) -> request.getTrainerUsernames().contains(tp.getTrainer().getUser().getUserName()))
                .collect(Collectors.toList());
        Set<String> existingPartnershipsTrainerNames = existingPartnerships.stream()
                .map(tp -> tp.getTrainer().getUser().getUserName())
                .collect(Collectors.toSet());
        Set<String> pendingCreationNames = request.getTrainerUsernames().stream()
                .filter(s -> !existingPartnershipsTrainerNames.contains(s))
                .collect(Collectors.toSet());
        //STEP 2 DONE
        List<Trainer> pendingCreationTrainers = pendingCreationNames.stream()
                .map(s -> trainerHibernateRepository.findTrainerByUsername(s).orElseThrow(() -> new NoSuchEntityException("trainer " + s + " not found")))
                .peek(t -> {
                    if (!t.getUser().isActive()) throw new NoPermissionException("trainer " + t.getUser().getUserName() + " is inactive");
                })
                .collect(Collectors.toList());
        //STEP 3 DONE
        //TODO ADD BATCH SAVE HERE
        pendingRemoval.forEach(tp -> {
                    tp.setRemoved(true);
                    trainingPartnershipHibernateRepository.saveAndFlush(tp);
                });
        pendingReAddition.forEach(tp -> {
            tp.setRemoved(false);
            trainingPartnershipHibernateRepository.saveAndFlush(tp);
        });
        pendingCreationTrainers.stream()
                .map(t -> TrainingPartnership.builder()
                        .trainee(trainee)
                        .trainer(t)
                        .isRemoved(false)
                        .build())
                .forEach(trainingPartnershipHibernateRepository::saveAndFlush);
        //STEP 4 DONE

        return UpdateTrainingPartnershipListResponse.builder()
                .trainersList(trainingPartnershipHibernateRepository.findPartnershipByTraineeUsername(authUsername)
                        .stream()
                        .filter(tp -> !tp.isRemoved())
                        .filter(tp -> tp.getTrainer().getUser().isActive())
                        .map(tp -> converter.convert(tp.getTrainer(), TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();

    }
}
