package org.example.converters;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.TraineeShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeShortInfoResponseConverterTest {

    TraineeShortInfoResponseConverter converter = new TraineeShortInfoResponseConverter();

    @Test
    void convert() {
        String firstName = "firstName", lastName = "lastName", userName = "userName";
        Trainee trainee = Trainee.builder()
                .user(User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userName(userName)
                        .build())
                .build();
        TraineeShortInfoResponse response = converter.convert(trainee);
        assert response != null;
        assertEquals(firstName,response.getFirstName());
        assertEquals(lastName,response.getLastName());
        assertEquals(userName,response.getUsername());
    }
}