package org.example.controller;


import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.trainer.UpdateMentorProfileRequest;
import org.example.requests_responses.training.GetMentorTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.service.MentorService;
import org.example.service.TrainingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class MentorControllerTest {

    @Mock
    private MentorService mentorService;
    @Mock
    private TrainingService trainingService;
    @InjectMocks
    MentorController mentorController;

    final String USER = "user";

    @Test
    void getTrainer() {
        MentorFullInfoResponse responseBody = new MentorFullInfoResponse();

        Mockito.when(mentorService.get(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = mentorController.getMentor(USER);
        Mockito.verify(mentorService).get(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updateTrainer() {
        UpdateMentorProfileRequest body = new UpdateMentorProfileRequest();
        MentorFullInfoResponse responseBody = new MentorFullInfoResponse();

        Mockito.when(mentorService.update(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = mentorController.updateMentor(body, USER);
        Mockito.verify(mentorService).update(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void getTrainings() {
        GetMentorTrainingsRequest body = new GetMentorTrainingsRequest();
        MultipleTrainingInfoResponse responseBody = new MultipleTrainingInfoResponse();

        Mockito.when(trainingService.getByMentor(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = mentorController.getTrainings(body, USER);
        Mockito.verify(trainingService).getByMentor(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

}