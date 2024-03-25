package org.example.converters;

import org.example.domain_entities.Mentor;
import org.example.domain_entities.Specialisation;
import org.example.domain_entities.User;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MentorShortInfoResponseConverterTest {

    MentorShortInfoResponseConverter converter = new MentorShortInfoResponseConverter();

    @Test
    void convert() {
        String firstName = "firstName", lastName = "lastName", userName = "userName";
        String type = "type";
        Mentor mentor = Mentor.builder()
                .user(User.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .userName(userName)
                        .build())
                .specialization(Specialisation.builder().specialisationName(type).build())
                .build();
        MentorShortInfoResponse response = converter.convert(mentor);
        assert response != null;
        assertEquals(firstName,response.getFirstName());
        assertEquals(lastName,response.getLastName());
        assertEquals(userName,response.getUsername());
        assertEquals(type, response.getSpecialization());
    }
}