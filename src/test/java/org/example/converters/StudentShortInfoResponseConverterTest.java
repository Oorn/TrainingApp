package org.example.converters;

import org.example.domain_entities.Student;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.StudentShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentShortInfoResponseConverterTest {

    StudentShortInfoResponseConverter converter = new StudentShortInfoResponseConverter();

    @Test
    void convert() {
        String firstName = "firstName", lastName = "lastName", userName = "userName";
        Student student = Student.builder()
                .user(User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userName(userName)
                        .build())
                .build();
        StudentShortInfoResponse response = converter.convert(student);
        assert response != null;
        assertEquals(firstName,response.getFirstName());
        assertEquals(lastName,response.getLastName());
        assertEquals(userName,response.getUsername());
    }
}