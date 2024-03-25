package org.example.service.impl;

import org.example.domain_entities.Student;
import org.example.domain_entities.Training;
import org.example.domain_entities.Partnership;
import org.example.domain_entities.User;
import org.example.repository.StudentHibernateRepository;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainee.UpdateStudentProfileRequest;
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
class StudentServiceImplTest {

    @Mock
    private CredentialsService credentialsService;
    @Mock
    private CredentialsServiceUtils credentialsServiceUtils;
    @Mock
    private StudentHibernateRepository traineeRepository;
    @Mock
    private ConversionService converter;

    @InjectMocks
    StudentServiceImpl service;

    @Test
    void create() {
        String firstName = "first", lastName = "last";
        String username = "username", password = "password";
        CreateStudentRequest request = CreateStudentRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .build();
        Student student = mock(Student.class);
        User user = mock(User.class);

        when(converter.convert(request, Student.class)).thenReturn(student);
        when(student.getUser()).thenReturn(user);
        when(credentialsService.generateUsername(firstName,lastName)).thenReturn(username);
        when(credentialsServiceUtils.generateRandomPassword()).thenReturn(password);

        CredentialsResponse response = service.create(request);

        assertEquals(username,response.getUsername());
        assertEquals(password, response.getPassword());
        verify(credentialsService, times(1)).generateUsername(firstName, lastName);
        verify(credentialsServiceUtils, times(1)).generateRandomPassword();
        verify(traineeRepository, times(1)).saveAndFlush(student);
        verify(credentialsServiceUtils, times(1)).setUserPassword(user, password);
        verify(user, times(1)).setUserName(username);
    }

    @Test
    void get() {
        String username = "username";
        Student student = mock(Student.class);
        StudentFullInfoResponse expectedResponse = mock(StudentFullInfoResponse.class);

        when(traineeRepository.findStudentByUsername(username)).thenReturn(Optional.of(student));
        when(converter.convert(student, StudentFullInfoResponse.class)).thenReturn(expectedResponse);

        StudentFullInfoResponse response = service.get(username);
        assertEquals(expectedResponse, response);
        verify(traineeRepository, times(1)).findStudentByUsername(username);
        verify(converter, times(1)).convert(student, StudentFullInfoResponse.class);
    }

    @Test
    void update() {
        String username = "username";
        String address = "address", firstName = "firstName", lastName = "lastName";
        boolean isActive = false;
        Instant dateOfBirth = Instant.now();
        Partnership partnership = Partnership.builder()
                .isRemoved(false)
                .build();
        HashSet<Partnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Student student = Student.builder()
                .partnerships(partnerships)
                .user(User.builder().build())
                .build();
        UpdateStudentProfileRequest request = UpdateStudentProfileRequest.builder()
                .address(address)
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .firstName(firstName)
                .lastName(lastName)
                .isActive(isActive)
                .build();
        StudentFullInfoResponse expectedResponse = new StudentFullInfoResponse();

        when(traineeRepository.findStudentByUsername(username)).thenReturn(Optional.of(student));
        when(converter.convert(student, StudentFullInfoResponse.class)).thenReturn(expectedResponse);

        StudentFullInfoResponse response = service.update(username, request);
        assertEquals(address, student.getAddress());
        assertEquals(dateOfBirth, student.getDateOfBirth().toInstant());
        assertEquals(firstName, student.getUser().getFirstName());
        assertEquals(lastName, student.getUser().getLastName());
        assertEquals(isActive, student.getUser().isActive());
        assertEquals(isActive, partnership.isRemoved());
        verify(traineeRepository, times(1)).saveAndFlush(student);
    }

    @Test
    void delete() {
        String username = "username";
        Partnership partnership = Partnership.builder()
                .isRemoved(false)
                .trainings(new HashSet<>())
                .build();
        HashSet<Partnership> partnerships = new HashSet<>();
        partnerships.add(partnership);
        Training training = Training.builder()
                .isRemoved(false)
                .build();
        partnership.getTrainings().add(training);
        Student student = Student.builder()
                .partnerships(partnerships)
                .user(User.builder().build())
                .build();

        when(traineeRepository.findStudentByUsername(username)).thenReturn(Optional.of(student));

        service.delete(username);
        verify(traineeRepository, times(1)).saveAndFlush(student);
        assert student.isRemoved();
        assert student.getUser().isRemoved();
        assert partnership.isRemoved();
        assert training.isRemoved();

    }
}