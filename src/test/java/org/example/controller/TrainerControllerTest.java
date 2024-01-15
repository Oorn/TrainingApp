package org.example.controller;


import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.service.TrainerService;
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
class TrainerControllerTest {

    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    TrainerController trainerController;

    final String USER = "user";

    @Test
    void getTrainer() {
        TrainerFullInfoResponse responseBody = new TrainerFullInfoResponse();

        Mockito.when(trainerService.get(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = trainerController.getTrainer(USER);
        Mockito.verify(trainerService).get(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updateTrainer() {
        UpdateTrainerProfileRequest body = new UpdateTrainerProfileRequest();
        TrainerFullInfoResponse responseBody = new TrainerFullInfoResponse();

        Mockito.when(trainerService.update(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = trainerController.updateTrainer(body, USER);
        Mockito.verify(trainerService).update(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void getTrainings() {
        GetTrainerTrainingsRequest body = new GetTrainerTrainingsRequest();
        MultipleTrainingInfoResponse responseBody = new MultipleTrainingInfoResponse();

        Mockito.when(trainingService.getByTrainer(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = trainerController.getTrainings(body, USER);
        Mockito.verify(trainingService).getByTrainer(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

}