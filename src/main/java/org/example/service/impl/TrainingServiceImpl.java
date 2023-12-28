package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.*;
import org.example.exceptions.BadRequestException;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.*;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.*;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Setter(onMethod_={@Autowired})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private TraineeRepository traineeRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingRepository trainingRepository;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public boolean create(CreateTrainingRequest request) {
        TrainingPartnership partnership = trainingPartnershipRepository.getByTraineeTrainer(request.getTraineeUsername(), request.getTrainerUsername()).orElseThrow(()->new NoSuchEntityException("training partnership not found"));

        if (partnership.getTrainee().getUser().isRemoved())
            throw new RemovedEntityException("trainee has been removed");
        if (!partnership.getTrainer().getUser().isActive())
            throw new RemovedEntityException("trainer is inactive");
        if (!partnership.getTrainee().getUser().isActive())
            throw new RemovedEntityException("trainee is inactive");
        if (partnership.isRemoved())
            throw new RemovedEntityException("training partnership has been removed (and can be restored)");

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
            throw new BadRequestException("requires trainee username");
        Optional<Trainee> traineeOptional = traineeRepository.get(filter.getTraineeName());
        if (traineeOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (traineeOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");


        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingRepository.getTrainingsByFilter(filter).stream()
                        .filter(t->!t.getTrainingPartnership().getTrainer().isRemoved()) //not needed because trainers can't be removed
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public MultipleTrainingInfoResponse getByTrainer(GetTrainerTrainingsRequest request) {
        TrainingSearchFilter filter = Objects.requireNonNull(converter.convert(request, TrainingSearchFilter.class));
        if (filter.getTrainerName() == null)
            throw new BadRequestException("requires trainer username");
        Optional<Trainer> trainerOptional = trainerRepository.get(filter.getTrainerName());
        if (trainerOptional.isEmpty())
            throw new NoSuchElementException("trainee not found");
        if (trainerOptional.get().isRemoved())
            throw new RemovedEntityException("trainee has been removed");

        return MultipleTrainingInfoResponse.builder()
                .trainings(trainingRepository.getTrainingsByFilter(filter).stream()
                        .filter(t->!t.getTrainingPartnership().getTrainee().isRemoved()) //don't show trainings from removed trainees
                        .map(t->converter.convert(t, TrainingInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
