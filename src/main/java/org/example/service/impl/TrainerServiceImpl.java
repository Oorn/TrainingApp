package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.TrainerRepository;
import org.example.repository.UserRepository;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Setter(onMethod_={@Autowired})
    private CredentialsServiceUtils credentialsServiceUtils;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    @Setter(onMethod_={@Autowired})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;
    @Override
    public CredentialsResponse create(CreateTrainerRequest request) {
        String username = credentialsService.generateUsername(request.getFirstName(), request.getLastName());
        String password = credentialsServiceUtils.generateRandomPassword();

        Trainer newTrainer = Objects.requireNonNull(converter.convert(request, Trainer.class));
        newTrainer.getUser().setUserName(username);
        credentialsServiceUtils.setUserPassword(newTrainer.getUser(),password);

        trainerRepository.save(newTrainer);

        return CredentialsResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public TrainerFullInfoResponse get(String username) {
        Trainer trainer = trainerRepository.get(username).orElse(null);
        if (trainer == null)
            throw new NoSuchEntityException("trainer not found");
        if (trainer.isRemoved())
            throw new RemovedEntityException("trainer has been removed");
        return converter.convert(trainer, TrainerFullInfoResponse.class);
    }

    @Override
    public TrainerFullInfoResponse update(String authUsername, UpdateTrainerProfileRequest request) {
        Trainer trainer = trainerRepository.get(authUsername).orElse(null);
        if (trainer == null)
            throw new NoSuchEntityException("trainer not found");
        if (trainer.isRemoved())
            throw new RemovedEntityException("trainer has been removed");

        trainer.getUser().setFirstName(request.getFirstName());
        trainer.getUser().setLastName(request.getLastName());
        trainer.getUser().setActive(request.isActive());
        if (request.isActive())
            trainer.getTrainingPartnerships().forEach(tp->tp.setRemoved(true)); //automatically remove partnerships of inactive users (but not trainings)

        trainerRepository.save(trainer);

        return converter.convert(trainer, TrainerFullInfoResponse.class);
    }
}
