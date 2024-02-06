package org.example.service.impl;

import org.example.domain_entities.*;
import org.example.repository.MentorHibernateRepository;
import org.example.requests_responses.trainer.CreateMentorRequest;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.trainer.UpdateMentorProfileRequest;
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
class MentorServiceImplTest {
    @Mock
    private CredentialsService credentialsService;
    @Mock
    private CredentialsServiceUtils credentialsServiceUtils;
    @Mock
    private MentorHibernateRepository trainerRepository;
    @Mock
    private ConversionService converter;

    @InjectMocks
    MentorServiceImpl service;

    @Test
    void create() {
        String firstName = "first", lastName = "last", specialization = "type";
        String username = "username", password = "password";
        CreateMentorRequest request = CreateMentorRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .specialisation(specialization)
                .build();
        Mentor mentor = mock(Mentor.class);
        User user = mock(User.class);

        when(converter.convert(request, Mentor.class)).thenReturn(mentor);
        when(mentor.getUser()).thenReturn(user);
        when(credentialsService.generateUsername(firstName,lastName)).thenReturn(username);
        when(credentialsServiceUtils.generateRandomPassword()).thenReturn(password);

        CredentialsResponse response = service.create(request);

        assertEquals(username,response.getUsername());
        assertEquals(password, response.getPassword());
        verify(credentialsService, times(1)).generateUsername(firstName, lastName);
        verify(credentialsServiceUtils, times(1)).generateRandomPassword();
        verify(trainerRepository, times(1)).saveAndFlush(mentor);
        verify(credentialsServiceUtils, times(1)).setUserPassword(user, password);
        verify(user, times(1)).setUserName(username);
    }

    @Test
    void get() {
        String username = "username";
        Mentor mentor = mock(Mentor.class);
        MentorFullInfoResponse expectedResponse = mock(MentorFullInfoResponse.class);

        when(trainerRepository.findMentorByUsername(username)).thenReturn(Optional.of(mentor));
        when(converter.convert(mentor, MentorFullInfoResponse.class)).thenReturn(expectedResponse);

        MentorFullInfoResponse response = service.get(username);
        assertEquals(expectedResponse, response);
        verify(trainerRepository, times(1)).findMentorByUsername(username);
        verify(converter, times(1)).convert(mentor, MentorFullInfoResponse.class);
    }

    @Test
    void update() {
        String username = "username";
        String firstName = "firstName", lastName = "lastName";
        boolean isActive = false;
        Partnership partnership = Partnership.builder()
                .isRemoved(false)
                .build();
        HashSet<Partnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Mentor mentor = Mentor.builder()
                .partnerships(partnerships)
                .user(User.builder().build())
                .build();
        UpdateMentorProfileRequest request = UpdateMentorProfileRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
        MentorFullInfoResponse expectedResponse = new MentorFullInfoResponse();

        when(trainerRepository.findMentorByUsername(username)).thenReturn(Optional.of(mentor));
        when(converter.convert(mentor, MentorFullInfoResponse.class)).thenReturn(expectedResponse);

        MentorFullInfoResponse response = service.update(username, request);

        assertEquals(firstName, mentor.getUser().getFirstName());
        assertEquals(lastName, mentor.getUser().getLastName());
        assertEquals(isActive, mentor.getUser().isActive());
        assertEquals(isActive, partnership.isRemoved());
        verify(trainerRepository, times(1)).saveAndFlush(mentor);
    }
}