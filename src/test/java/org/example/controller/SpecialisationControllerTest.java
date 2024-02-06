package org.example.controller;

import org.example.service.SpecialisationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Objects;

@ExtendWith(MockitoExtension.class)
class SpecialisationControllerTest {

    @Mock
    private SpecialisationService specialisationService;
    @InjectMocks
    SpecialisationController specialisationController;

    @Test
    void createTrainingType() {
        String type = "type";

        Mockito.when(specialisationService.create(type)).thenReturn(true);

        ResponseEntity<Object> response = specialisationController.createSpecialisation(type);
        Mockito.verify(specialisationService).create(type);
        assert response.getStatusCode().is2xxSuccessful();
    }

    @Test
    void getAllTrainingTypes() {
        ArrayList<String> types = new ArrayList<>();

        Mockito.when(specialisationService.get()).thenReturn(types);

        ResponseEntity<Object> response = specialisationController.getAllSpecialisations();
        Mockito.verify(specialisationService).get();
        assert response.getStatusCode().is2xxSuccessful();
        assert Objects.equals(response.getBody(), types);

    }
}