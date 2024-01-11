package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
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

    @Setter(onMethod_={@Autowired})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private TraineeRepository traineeRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public AvailableTrainersResponse getNotAssignedTrainers(String traineeUsername) {
        if (traineeRepository.get(traineeUsername).isEmpty())
            throw new NoSuchEntityException("trainee not found");
        Set<Trainer> assignedTrainers = trainingPartnershipRepository.getByTraineeName(traineeUsername).stream()
                .filter(t->!t.isRemoved())
                .map(TrainingPartnership::getTrainer)
                .collect(Collectors.toSet());
        return AvailableTrainersResponse.builder()
                .trainers(trainerRepository.getAll().stream()
                        .filter(t->!t.isRemoved())
                        .filter(t->t.getUser().isActive())
                        .filter(t->!assignedTrainers.contains(t))
                        .map(t->converter.convert(t, TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public UpdateTrainingPartnershipListResponse updateTraineeTrainerList(String authUsername, UpdateTrainingPartnershipListRequest request) {
        List<Trainer> trainers = request.getTrainerUsernames().stream()
                .map(t->trainerRepository.get(t).orElseThrow(()->new NoSuchEntityException("trainer not found")))
                .peek(t->{
                    if (t.isRemoved()) throw new RemovedEntityException("trainer has been removed"); //is not necessary, trainers cannot be removed
                })
                .peek(t->{
                    if (!t.getUser().isActive()) throw new RemovedEntityException("trainer is inactive");
                })
                .filter(t->t.getUser().isActive()) //inactive trainers are silently ignored without raising error
                .collect(Collectors.toList());
        return UpdateTrainingPartnershipListResponse.builder()
                .trainersList(trainingPartnershipRepository.updateAndReturnListForTrainee(authUsername, trainers)
                        .stream()
                        .filter(t->!t.isRemoved())
                        .map(TrainingPartnership::getTrainer)
                        .filter(t->t.getUser().isActive())
                        .map(t->converter.convert(t, TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
