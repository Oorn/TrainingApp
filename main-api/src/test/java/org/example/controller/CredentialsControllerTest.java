package org.example.controller;

import org.example.exceptions.NoPermissionException;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainer.CreateMentorRequest;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.security.JWTUtils;
import org.example.service.CredentialsService;
import org.example.service.StudentService;
import org.example.service.MentorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CredentialsControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private MentorService mentorService;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    CredentialsController credentialsController;


    @Test
    void createTrainee() {
        CreateStudentRequest req = new CreateStudentRequest();
        Mockito.when(studentService.create(req)).thenReturn(new CredentialsResponse());

        ResponseEntity<Object> response = credentialsController.createStudent(req);
        Mockito.verify(studentService).create(req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void createTrainer() {
        CreateMentorRequest req = new CreateMentorRequest();
        Mockito.when(mentorService.create(req)).thenReturn(new CredentialsResponse());

        ResponseEntity<Object> response = credentialsController.createMentor(req);
        Mockito.verify(mentorService).create(req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void loginTrainee() {
        String user = "user", password = "password", token = "token";
        Mockito.when(credentialsService.validateUsernamePassword(user,password))
                .thenReturn(true);
        Mockito.when(studentService.get("user"))
                .thenReturn(new StudentFullInfoResponse());
        Mockito.when(jwtUtils.generateToken(Mockito.any()))
                .thenReturn(token);

        ResponseEntity<Object> response = credentialsController.loginTrainee(password, user);
        Mockito.verify(credentialsService).validateUsernamePassword(user, password);
        assert response.getStatusCode().is2xxSuccessful();
        assertNotNull(response.getHeaders());
        assertNotNull(response.getHeaders().get("Auth-Token"));
        assert response.getHeaders().get("Auth-Token").stream().anyMatch(s->s.equals(token));

    }

    @Test
    void loginTraineeFail() {
        String user = "user", password = "password";
        Mockito.when(credentialsService.validateUsernamePassword(user,password))
                .thenThrow(new NoPermissionException("ex"));

        Assertions.assertThrows(NoPermissionException.class, () ->
                credentialsController.loginTrainee(password, user));
        Mockito.verify(credentialsService).validateUsernamePassword(user, password);
    }

    @Test
    void loginTrainer() {
        String user = "user", password = "password", token = "token";
        Mockito.when(credentialsService.validateUsernamePassword(user,password))
                .thenReturn(true);
        Mockito.when(mentorService.get("user"))
                .thenReturn(new MentorFullInfoResponse());
        Mockito.when(jwtUtils.generateToken(Mockito.any()))
                .thenReturn(token);

        ResponseEntity<Object> response = credentialsController.loginTrainer(password, user);
        Mockito.verify(credentialsService).validateUsernamePassword(user, password);
        assert response.getStatusCode().is2xxSuccessful();
        assertNotNull(response.getHeaders());
        assertNotNull(response.getHeaders().get("Auth-Token"));
        assert response.getHeaders().get("Auth-Token").stream().anyMatch(s->s.equals(token));
    }

    @Test
    void updateTraineePassword() {
        String user = "user";
        UpdateCredentialsRequest req = new UpdateCredentialsRequest();
        Mockito.when(credentialsService.updateCredentials(user, req))
                .thenReturn(true);

        ResponseEntity<Object> response = credentialsController.updateStudentPassword(req, user);
        Mockito.verify(credentialsService).updateCredentials(user, req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void updateTrainerPassword() {
        String user = "user";
        UpdateCredentialsRequest req = new UpdateCredentialsRequest();
        Mockito.when(credentialsService.updateCredentials(user, req))
                .thenReturn(true);

        ResponseEntity<Object> response = credentialsController.updateMentorPassword(req, user);
        Mockito.verify(credentialsService).updateCredentials(user, req);
        assert response.getStatusCode().is2xxSuccessful();
    }
}