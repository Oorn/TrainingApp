package org.example.controller;

import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.service.TraineeService;
import org.example.service.TrainingPartnershipService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {

    @Mock
    private TraineeService traineeService;

    @Mock
    private TrainingPartnershipService partnershipService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    TraineeController traineeController;

    final String USER = "user";

    @Test
    void getTrainee() {
        TraineeFullInfoResponse responseBody = new TraineeFullInfoResponse();

        Mockito.when(traineeService.get(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = traineeController.getTrainee(USER);
        Mockito.verify(traineeService).get(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updateTrainee() {
        UpdateTraineeProfileRequest body = new UpdateTraineeProfileRequest();
        TraineeFullInfoResponse responseBody = new TraineeFullInfoResponse();

        Mockito.when(traineeService.update(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = traineeController.updateTrainee(body, USER);
        Mockito.verify(traineeService).update(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void deleteTrainee() {
        Mockito.when(traineeService.delete(USER)).thenReturn(true);

        ResponseEntity<Object> response = traineeController.deleteTrainee(USER);
        Mockito.verify(traineeService).delete(USER);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void getFreeTrainers() {
        AvailableTrainersResponse responseBody = new AvailableTrainersResponse();

        Mockito.when(partnershipService.getNotAssignedTrainers(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = traineeController.getFreeTrainers(USER);
        Mockito.verify(partnershipService).getNotAssignedTrainers(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updatePartnerships() {

        UpdateTrainingPartnershipListRequest body = new UpdateTrainingPartnershipListRequest();
        UpdateTrainingPartnershipListResponse responseBody = new UpdateTrainingPartnershipListResponse();

        Mockito.when(partnershipService.updateTraineeTrainerList(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = traineeController.updatePartnerships(body, USER);
        Mockito.verify(partnershipService).updateTraineeTrainerList(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void getTrainings() {
        GetTraineeTrainingsRequest body = new GetTraineeTrainingsRequest();
        MultipleTrainingInfoResponse responseBody = new MultipleTrainingInfoResponse();

        Mockito.when(trainingService.getByTrainee(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = traineeController.getTrainings(body, USER);
        Mockito.verify(trainingService).getByTrainee(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

}