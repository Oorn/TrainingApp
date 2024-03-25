package org.example.controller;

import org.example.requests_responses.training.CreateTrainingForStudentRequest;
import org.example.requests_responses.training.CreateTrainingForMentorRequest;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TrainingControllerTest {

    @Mock
    private TrainingService trainingService;
    @InjectMocks
    TrainingController trainingController;

    final String USER = "user";

    @Test
    void createTraineeTraining() {
        CreateTrainingForStudentRequest body = new CreateTrainingForStudentRequest();

        Mockito.when(trainingService.create(USER, body)).thenReturn(true);

        ResponseEntity<Object> response = trainingController.createStudentTraining(body, USER);
        Mockito.verify(trainingService).create(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void createTrainerTraining() {
        CreateTrainingForMentorRequest body = new CreateTrainingForMentorRequest();

        Mockito.when(trainingService.create(USER, body)).thenReturn(true);

        ResponseEntity<Object> response = trainingController.createMentorTraining(body, USER);
        Mockito.verify(trainingService).create(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
    }
}