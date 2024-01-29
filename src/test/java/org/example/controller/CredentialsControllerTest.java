package org.example.controller;

import lombok.Setter;
import org.example.exceptions.IllegalStateException;
import org.example.exceptions.NoPermissionException;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.security.JWTUtils;
import org.example.service.CredentialsService;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class CredentialsControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainerService trainerService;

    @Mock
    private CredentialsService credentialsService;

    @Mock
    private JWTUtils jwtUtils;

    @InjectMocks
    CredentialsController credentialsController;


    @Test
    void createTrainee() {
        CreateTraineeRequest req = new CreateTraineeRequest();
        Mockito.when(traineeService.create(req)).thenReturn(new CredentialsResponse());

        ResponseEntity<Object> response = credentialsController.createTrainee(req);
        Mockito.verify(traineeService).create(req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void createTrainer() {
        CreateTrainerRequest req = new CreateTrainerRequest();
        Mockito.when(trainerService.create(req)).thenReturn(new CredentialsResponse());

        ResponseEntity<Object> response = credentialsController.createTrainer(req);
        Mockito.verify(trainerService).create(req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void loginTrainee() {
        String user = "user", password = "password", token = "token";
        Mockito.when(credentialsService.validateUsernamePassword(user,password))
                .thenReturn(true);
        Mockito.when(traineeService.get("user"))
                .thenReturn(new TraineeFullInfoResponse());
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
        Mockito.when(trainerService.get("user"))
                .thenReturn(new TrainerFullInfoResponse());
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

        ResponseEntity<Object> response = credentialsController.updateTraineePassword(req, user);
        Mockito.verify(credentialsService).updateCredentials(user, req);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void updateTrainerPassword() {
        String user = "user";
        UpdateCredentialsRequest req = new UpdateCredentialsRequest();
        Mockito.when(credentialsService.updateCredentials(user, req))
                .thenReturn(true);

        ResponseEntity<Object> response = credentialsController.updateTrainerPassword(req, user);
        Mockito.verify(credentialsService).updateCredentials(user, req);
        assert response.getStatusCode().is2xxSuccessful();
    }
}