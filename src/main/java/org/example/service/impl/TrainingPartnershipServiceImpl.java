package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
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
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TrainingPartnershipServiceImpl implements TrainingPartnershipService {

    @Setter(onMethod_={@Autowired})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public AvailableTrainersResponse getNotAssignedTrainers(String traineeUsername) {
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
    public UpdateTrainingPartnershipListResponse updateTraineeTrainerList(UpdateTrainingPartnershipListRequest request) {
        List<Trainer> trainers = request.getTrainerUsername().stream()
                .map(t->trainerRepository.get(t).orElseThrow())
                .peek(t->{
                    if (t.isRemoved()) throw new NoSuchElementException();
                })
                .filter(t->t.getUser().isActive()) //todo better handling of inactive trainers in request than ignoring?
                .collect(Collectors.toList());
        return UpdateTrainingPartnershipListResponse.builder()
                .trainersList(trainingPartnershipRepository.updateAndReturnListForTrainee(request.getUsername(), trainers)
                        .stream()
                        .filter(t->!t.isRemoved())
                        .map(TrainingPartnership::getTrainer)
                        .filter(t->t.getUser().isActive())
                        .map(t->converter.convert(t, TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}