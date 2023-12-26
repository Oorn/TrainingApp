package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
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
            return null; //todo throw not found exception if null
        if (trainer.isRemoved())
            return null; //todo throw removed exception
        return converter.convert(trainer, TrainerFullInfoResponse.class);
    }

    @Override
    public TrainerFullInfoResponse update(UpdateTrainerProfileRequest request) {
        Trainer trainer = trainerRepository.get(request.getUsername()).orElse(null);
        if (trainer == null)
            return null; //todo throw not found exception if null
        if (trainer.isRemoved())
            return null; //todo throw removed exception

        trainer.getUser().setFirstName(request.getFirstName());
        trainer.getUser().setLastName(request.getLastName());
        trainer.getUser().setActive(request.isActive());

        trainerRepository.save(trainer);

        return converter.convert(trainer, TrainerFullInfoResponse.class);
    }
}
