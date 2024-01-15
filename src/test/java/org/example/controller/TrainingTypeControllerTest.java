package org.example.controller;

import org.example.service.TrainingTypeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {

    @Mock
    private TrainingTypeService trainingTypeService;
    @InjectMocks
    TrainingTypeController trainingTypeController;

    @Test
    void createTrainingType() {
        String type = "type";

        Mockito.when(trainingTypeService.create(type)).thenReturn(true);

        ResponseEntity<Object> response = trainingTypeController.createTrainingType(type);
        Mockito.verify(trainingTypeService).create(type);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void getAllTrainingTypes() {
        ArrayList<String> types = new ArrayList<>();

        Mockito.when(trainingTypeService.get()).thenReturn(types);

        ResponseEntity<Object> response = trainingTypeController.getAllTrainingTypes();
        Mockito.verify(trainingTypeService).get();
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), types);

    }
}