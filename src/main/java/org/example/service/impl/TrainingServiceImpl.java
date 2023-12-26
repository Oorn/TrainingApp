package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Training;
import org.example.domain_entities.TrainingPartnership;
import org.example.domain_entities.TrainingType;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.TrainingRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.*;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Setter(onMethod_={@Autowired})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private TrainerRepository traineeRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingRepository trainingRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingTypeRepository trainingTypeRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public boolean create(CreateTrainingRequest request) {
        TrainingPartnership partnership = trainingPartnershipRepository.getByTraineeTrainer(request.getTraineeUsername(), request.getTrainerUsername()).orElseThrow();
        if (partnership.isRemoved())
            throw new NoSuchElementException();
        if (!partnership.getTrainer().getUser().isActive())
            throw new NoSuchElementException();
        if (!partnership.getTrainee().getUser().isActive())
            throw new NoSuchElementException();
        if (partnership.getTrainee().getUser().isRemoved())
            throw new NoSuchElementException(); //todo more granular exceptions

        TrainingType trainingType = trainingTypeRepository.get(request.getType()).orElseThrow();
        if (trainingType != partnership.getTrainer().getSpecialization())
            throw new IllegalStateException(); //todo better exception, partnership mismatches with trainer

        Training training = Training.builder()
                .trainingName(request.getName())
                .isRemoved(false)
                .trainingPartnership(partnership)
                .trainingDateFrom(request.getDate())
                .trainingDateTo(request.getDate().plus(request.getDuration()))
                .build();

        trainingRepository.save(training);
        return true;
    }

    @Override
    public MultipleTrainingInfoResponse getByTrainee(GetTraineeTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        if (filter.getTraineeName() == null)
            throw new IllegalStateException(); //todo better exception
        if (traineeRepository.get(filter.getTraineeName()).isEmpty())
            throw new NoSuchElementException(); //todo better exception

        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingRepository.getTrainingsByFilter(filter).stream()
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public MultipleTrainingInfoResponse getByTrainer(GetTrainerTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        if (filter.getTrainerName() == null)
            throw new IllegalStateException(); //todo better exception
        if (trainerRepository.get(filter.getTrainerName()).isEmpty())
            throw new NoSuchElementException(); //todo better exception

        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingRepository.getTrainingsByFilter(filter).stream()
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
