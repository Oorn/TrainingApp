package org.example.controller;

import org.example.requests_responses.training.CreateTrainingForTraineeRequest;
import org.example.requests_responses.training.CreateTrainingForTrainerRequest;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;
    @InjectMocks
    TrainingController trainingController;

    final String USER = "user";

    @Test
    void createTraineeTraining() {
        CreateTrainingForTraineeRequest body = new CreateTrainingForTraineeRequest();

        Mockito.when(trainingService.create(USER, body)).thenReturn(true);

        ResponseEntity<Object> response = trainingController.createTraineeTraining(body, USER);
        Mockito.verify(trainingService).create(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void createTrainerTraining() {
        CreateTrainingForTrainerRequest body = new CreateTrainingForTrainerRequest();

        Mockito.when(trainingService.create(USER, body)).thenReturn(true);

        ResponseEntity<Object> response = trainingController.createTrainerTraining(body, USER);
        Mockito.verify(trainingService).create(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
    }
}