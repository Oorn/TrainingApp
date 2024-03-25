package org.example.controller;

import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainee.UpdateStudentProfileRequest;
import org.example.requests_responses.training.GetStudentTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.service.StudentService;
import org.example.service.PartnershipService;
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
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private PartnershipService partnershipService;

    @Mock
    private TrainingService trainingService;

    @InjectMocks
    StudentController studentController;

    final String USER = "user";

    @Test
    void getTrainee() {
        StudentFullInfoResponse responseBody = new StudentFullInfoResponse();

        Mockito.when(studentService.get(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = studentController.getStudent(USER);
        Mockito.verify(studentService).get(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updateTrainee() {
        UpdateStudentProfileRequest body = new UpdateStudentProfileRequest();
        StudentFullInfoResponse responseBody = new StudentFullInfoResponse();

        Mockito.when(studentService.update(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = studentController.updateStudent(body, USER);
        Mockito.verify(studentService).update(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void deleteTrainee() {
        Mockito.when(studentService.delete(USER)).thenReturn(true);

        ResponseEntity<Object> response = studentController.deleteStudent(USER);
        Mockito.verify(studentService).delete(USER);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void getFreeTrainers() {
        AvailableTrainersResponse responseBody = new AvailableTrainersResponse();

        Mockito.when(partnershipService.getNotAssignedMentors(USER)).thenReturn(responseBody);

        ResponseEntity<Object> response = studentController.getFreeMentors(USER);
        Mockito.verify(partnershipService).getNotAssignedMentors(USER);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void updatePartnerships() {

        UpdateTrainingPartnershipListRequest body = new UpdateTrainingPartnershipListRequest();
        UpdateTrainingPartnershipListResponse responseBody = new UpdateTrainingPartnershipListResponse();

        Mockito.when(partnershipService.updateStudentMentorsList(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = studentController.updatePartnerships(body, USER);
        Mockito.verify(partnershipService).updateStudentMentorsList(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

    @Test
    void getTrainings() {
        GetStudentTrainingsRequest body = new GetStudentTrainingsRequest();
        MultipleTrainingInfoResponse responseBody = new MultipleTrainingInfoResponse();

        Mockito.when(trainingService.getByStudent(USER, body)).thenReturn(responseBody);

        ResponseEntity<Object> response = studentController.getTrainings(body, USER);
        Mockito.verify(trainingService).getByStudent(USER, body);
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), responseBody);
    }

}