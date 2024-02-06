package org.example.service.impl;

import org.example.domain_entities.*;
import org.example.repository.TrainerHibernateRepository;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.HashSet;
import java.util.Optional;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private CredentialsService credentialsService;
    @Mock
    private CredentialsServiceUtils credentialsServiceUtils;
    @Mock
    private TrainerHibernateRepository trainerRepository;
    @Mock
    private ConversionService converter;

    @InjectMocks
    TrainerServiceImpl service;

    @Test
    void create() {
        String firstName = "first", lastName = "last", specialization = "type";
        String username = "username", password = "password";
        CreateTrainerRequest request = CreateTrainerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .specialisation(specialization)
                .build();
        Trainer trainer = mock(Trainer.class);
        User user = mock(User.class);

        when(converter.convert(request, Trainer.class)).thenReturn(trainer);
        when(trainer.getUser()).thenReturn(user);
        when(credentialsService.generateUsername(firstName,lastName)).thenReturn(username);
        when(credentialsServiceUtils.generateRandomPassword()).thenReturn(password);

        CredentialsResponse response = service.create(request);

        assertEquals(username,response.getUsername());
        assertEquals(password, response.getPassword());
        verify(credentialsService, times(1)).generateUsername(firstName, lastName);
        verify(credentialsServiceUtils, times(1)).generateRandomPassword();
        verify(trainerRepository, times(1)).saveAndFlush(trainer);
        verify(credentialsServiceUtils, times(1)).setUserPassword(user, password);
        verify(user, times(1)).setUserName(username);
    }

    @Test
    void get() {
        String username = "username";
        Trainer trainer = mock(Trainer.class);
        TrainerFullInfoResponse expectedResponse = mock(TrainerFullInfoResponse.class);

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(converter.convert(trainer, TrainerFullInfoResponse.class)).thenReturn(expectedResponse);

        TrainerFullInfoResponse response = service.get(username);
        assertEquals(expectedResponse, response);
        verify(trainerRepository, times(1)).findTrainerByUsername(username);
        verify(converter, times(1)).convert(trainer,TrainerFullInfoResponse.class);
    }

    @Test
    void update() {
        String username = "username";
        String firstName = "firstName", lastName = "lastName";
        boolean isActive = false;
        TrainingPartnership partnership = TrainingPartnership.builder()
                .isRemoved(false)
                .build();
        HashSet<TrainingPartnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Trainer trainer = Trainer.builder()
                .trainingPartnerships(partnerships)
                .user(User.builder().build())
                .build();
        UpdateTrainerProfileRequest request = UpdateTrainerProfileRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
        TrainerFullInfoResponse expectedResponse = new TrainerFullInfoResponse();

        when(trainerRepository.findTrainerByUsername(username)).thenReturn(Optional.of(trainer));
        when(converter.convert(trainer, TrainerFullInfoResponse.class)).thenReturn(expectedResponse);

        TrainerFullInfoResponse response = service.update(username, request);

        assertEquals(firstName, trainer.getUser().getFirstName());
        assertEquals(lastName, trainer.getUser().getLastName());
        assertEquals(isActive, trainer.getUser().isActive());
        assertEquals(isActive, partnership.isRemoved());
        verify(trainerRepository, times(1)).saveAndFlush(trainer);
    }
}