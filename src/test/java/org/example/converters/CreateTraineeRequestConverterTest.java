package org.example.converters;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateTraineeRequestConverterTest {
    CreateTraineeRequestConverter converter = new CreateTraineeRequestConverter();

    @Test
    void convert() {

        String firstName = "firstName";
        String lastName = "lastName";
        String address = "address";
        Instant dateOfBirth = Instant.parse("2018-11-30T18:35:24.00Z");

        CreateTraineeRequest request = CreateTraineeRequest.builder()
                .firstName(firstName)
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .lastName(lastName)
                .address(address)
                .build();

        Trainee trainee = converter.convert(request);
        assert trainee != null;
        assertEquals(address, trainee.getAddress());
        assertEquals(dateOfBirth, trainee.getDateOfBirth().toInstant());
        assert !trainee.isRemoved();

        User user = trainee.getUser();
        assertEquals(trainee, user.getTraineeProfile());
        assert user.isActive();
        assert !user.isRemoved();
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }
}