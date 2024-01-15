package org.example.converters;

import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingType;
import org.example.domain_entities.User;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerShortInfoResponseConverterTest {

    TrainerShortInfoResponseConverter converter = new TrainerShortInfoResponseConverter();

    @Test
    void convert() {
        String firstName = "firstName", lastName = "lastName", userName = "userName";
        String type = "type";
        Trainer trainer = Trainer.builder()
                .user(User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userName(userName)
                        .build())
                .specialization(TrainingType.builder().trainingType(type).build())
                .build();
        TrainerShortInfoResponse response = converter.convert(trainer);
        assert response != null;
        assertEquals(firstName,response.getFirstName());
        assertEquals(lastName,response.getLastName());
        assertEquals(userName,response.getUsername());
        assertEquals(type, response.getSpecialization());
    }
}