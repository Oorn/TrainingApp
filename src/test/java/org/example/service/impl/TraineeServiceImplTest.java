package org.example.service.impl;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.Training;
import org.example.domain_entities.TrainingPartnership;
import org.example.domain_entities.User;
import org.example.repository.TraineeRepository;
import org.example.repository.impl.v2.hibernate.TraineeHibernateRepository;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {

    @Mock
    private CredentialsService credentialsService;
    @Mock
    private CredentialsServiceUtils credentialsServiceUtils;
    @Mock
    private TraineeHibernateRepository traineeRepository;
    @Mock
    private ConversionService converter;

    @InjectMocks
    TraineeServiceImpl service;

    @Test
    void create() {
        String firstName = "first", lastName = "last";
        String username = "username", password = "password";
        CreateTraineeRequest request = CreateTraineeRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        Trainee trainee = mock(Trainee.class);
        User user = mock(User.class);

        when(converter.convert(request, Trainee.class)).thenReturn(trainee);
        when(trainee.getUser()).thenReturn(user);
        when(credentialsService.generateUsername(firstName,lastName)).thenReturn(username);
        when(credentialsServiceUtils.generateRandomPassword()).thenReturn(password);

        CredentialsResponse response = service.create(request);

        assertEquals(username,response.getUsername());
        assertEquals(password, response.getPassword());
        verify(credentialsService, times(1)).generateUsername(firstName, lastName);
        verify(credentialsServiceUtils, times(1)).generateRandomPassword();
        verify(traineeRepository, times(1)).saveAndFlush(trainee);
        verify(credentialsServiceUtils, times(1)).setUserPassword(user, password);
        verify(user, times(1)).setUserName(username);
    }

    @Test
    void get() {
        String username = "username";
        Trainee trainee = mock(Trainee.class);
        TraineeFullInfoResponse expectedResponse = mock(TraineeFullInfoResponse.class);

        when(traineeRepository.findTraineeByUsername(username)).thenReturn(Optional.of(trainee));
        when(converter.convert(trainee, TraineeFullInfoResponse.class)).thenReturn(expectedResponse);

        TraineeFullInfoResponse response = service.get(username);
        assertEquals(expectedResponse, response);
        verify(traineeRepository, times(1)).findTraineeByUsername(username);
        verify(converter, times(1)).convert(trainee,TraineeFullInfoResponse.class);
    }

    @Test
    void update() {
        String username = "username";
        String address = "address", firstName = "firstName", lastName = "lastName";
        boolean isActive = false;
        Instant dateOfBirth = Instant.now();
        TrainingPartnership partnership = TrainingPartnership.builder()
                .isRemoved(false)
                .build();
        HashSet<TrainingPartnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Trainee trainee = Trainee.builder()
                .trainingPartnerships(partnerships)
                .user(User.builder().build())
                .build();
        UpdateTraineeProfileRequest request = UpdateTraineeProfileRequest.builder()
                .address(address)
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
        TraineeFullInfoResponse expectedResponse = new TraineeFullInfoResponse();

        when(traineeRepository.findTraineeByUsername(username)).thenReturn(Optional.of(trainee));
        when(converter.convert(trainee, TraineeFullInfoResponse.class)).thenReturn(expectedResponse);

        TraineeFullInfoResponse response = service.update(username, request);
        assertEquals(address, trainee.getAddress());
        assertEquals(dateOfBirth, trainee.getDateOfBirth().toInstant());
        assertEquals(firstName, trainee.getUser().getFirstName());
        assertEquals(lastName, trainee.getUser().getLastName());
        assertEquals(isActive, trainee.getUser().isActive());
        assertEquals(isActive, partnership.isRemoved());
        verify(traineeRepository, times(1)).saveAndFlush(trainee);
    }

    @Test
    void delete() {
        String username = "username";
        TrainingPartnership partnership = TrainingPartnership.builder()
                .isRemoved(false)
                .trainings(new HashSet<>())
                .build();
        HashSet<TrainingPartnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Training training = Training.builder()
                .isRemoved(false)
                .build();
        partnership.getTrainings().add(training);
        Trainee trainee = Trainee.builder()
                .trainingPartnerships(partnerships)
                .user(User.builder().build())
                .build();

        when(traineeRepository.findTraineeByUsername(username)).thenReturn(Optional.of(trainee));

        service.delete(username);
        verify(traineeRepository, times(1)).saveAndFlush(trainee);
        assert trainee.isRemoved();
        assert trainee.getUser().isRemoved();
        assert partnership.isRemoved();
        assert training.isRemoved();

    }
}