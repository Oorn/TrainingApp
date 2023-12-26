package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.repository.TraineeRepository;
import org.example.repository.UserRepository;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.example.service.IdentityProviderService;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class TraineeServiceImpl implements TraineeService {

    @Setter(onMethod_={@Autowired})
    private CredentialsServiceUtils credentialsServiceUtils;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    @Setter(onMethod_={@Autowired})
    private TraineeRepository traineeRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public CredentialsResponse create(CreateTraineeRequest request) {
        String username = credentialsService.generateUsername(request.getFirstName(), request.getLastName());
        String password = credentialsServiceUtils.generateRandomPassword();

        Trainee newTrainee = Objects.requireNonNull(converter.convert(request, Trainee.class));
        newTrainee.getUser().setUserName(username);
        credentialsServiceUtils.setUserPassword(newTrainee.getUser(),password);

        traineeRepository.save(newTrainee);

        return CredentialsResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public TraineeFullInfoResponse get(String username) {
        Trainee trainee = traineeRepository.get(username).orElse(null);
        if (trainee == null)
            return null; //todo throw not found exception if null
        if (trainee.isRemoved())
            return null; //todo throw removed exception
        return converter.convert(trainee, TraineeFullInfoResponse.class);
    }

    @Override
    public TraineeFullInfoResponse update(UpdateTraineeProfileRequest request) {
        Trainee trainee = traineeRepository.get(request.getUsername()).orElse(null);
        if (trainee == null)
            return null; //todo throw not found exception if null
        if (trainee.isRemoved())
            return null; //todo throw removed exception

        trainee.setAddress(request.getAddress());
        trainee.setDateOfBirth(request.getDateOfBirth());
        trainee.getUser().setFirstName(request.getFirstName());
        trainee.getUser().setLastName(request.getLastName());
        trainee.getUser().setActive(request.isActive());

        traineeRepository.save(trainee);

        return converter.convert(trainee, TraineeFullInfoResponse.class);
    }

    @Override
    public boolean delete(String username) {
        Trainee trainee = traineeRepository.get(username).orElse(null);
        if (trainee == null)
            return false;
        trainee.setRemoved(true);
        trainee.getUser().setRemoved(true);
        trainee.getTrainingPartnerships().forEach(t->t.setRemoved(true));
        trainee.getTrainingPartnerships().stream().
                flatMap(t->t.getTrainings().stream()).
                forEach(t->t.setRemoved(true));

        traineeRepository.save(trainee);
        return true;
    }
}
