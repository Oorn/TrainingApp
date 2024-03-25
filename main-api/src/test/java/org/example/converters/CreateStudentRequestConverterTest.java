package org.example.converters;

import org.example.domain_entities.Student;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateStudentRequestConverterTest {
    CreateStudentRequestConverter converter = new CreateStudentRequestConverter();

    @Test
    void convert() {

        String firstName = "firstName";
        String lastName = "lastName";
        String address = "address";
        Instant dateOfBirth = Instant.parse("2018-11-30T18:35:24.00Z");

        CreateStudentRequest request = CreateStudentRequest.builder()
                .firstName(firstName)
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .lastName(lastName)
                .address(address)
                .build();

        Student student = converter.convert(request);
        assert student != null;
        assertEquals(address, student.getAddress());
        assertEquals(dateOfBirth, student.getDateOfBirth().toInstant());
        assert !student.isRemoved();

        User user = student.getUser();
        assertEquals(student, user.getStudentProfile());
        assert user.isActive();
        assert !user.isRemoved();
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }
}