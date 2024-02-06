package org.example.converters;

import org.example.domain_entities.Mentor;
import org.example.domain_entities.Student;
import org.example.domain_entities.Partnership;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentFullInfoResponseConverterTest {

    @Mock
    private ConversionService conversionService;
    @InjectMocks
    StudentFullInfoResponseConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String userName = "username";
        String address = "address";

        boolean isRemoved = false;
        boolean isActive = true;
        Set<Partnership> partnerships = new HashSet<>();
        Instant dateOfBirth = Instant.parse("2018-11-30T18:35:24.00Z");
        MentorShortInfoResponse trainerShortInfo = new MentorShortInfoResponse();

        User user = User.builder()
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .isRemoved(isRemoved)
                .isActive(isActive)
                .build();
        Student student = Student.builder()
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .isRemoved(isRemoved)
                .address(address)
                .partnerships(partnerships)
                .user(user)
                .build();
        user.setStudentProfile(student);

        Mentor removedPartnershipMentor = Mentor.builder().build();


        User trainerUser = Mockito.mock(User.class);
        Mentor mentor = Mentor.builder().user(trainerUser).build();
        Mockito.when(trainerUser.isActive()).thenReturn(true);

        User inactiveTrainerUser = Mockito.mock(User.class);
        Mentor inactiveMentor = Mentor.builder().user(inactiveTrainerUser).build();
        Mockito.when(inactiveTrainerUser.isActive()).thenReturn(false);

        partnerships.add(Partnership.builder().id(1).isRemoved(true).student(student).mentor(removedPartnershipMentor).build());
        partnerships.add(Partnership.builder().id(2).isRemoved(false).student(student).mentor(inactiveMentor).build());
        partnerships.add(Partnership.builder().id(3).isRemoved(false).student(student).mentor(mentor).build());

        Mockito.when(conversionService.convert(mentor, MentorShortInfoResponse.class)).thenReturn(trainerShortInfo);

        StudentFullInfoResponse response = converter.convert(student);

        assert response != null;
        assertEquals(address, response.getAddress());
        assertEquals(dateOfBirth, response.getDateOfBirth().toInstant());
        assertEquals(firstName, response.getFirstName());
        assertEquals(isActive, response.isActive());
        assertEquals(lastName, response.getLastName());
        assertEquals(userName, response.getUsername());
        List<MentorShortInfoResponse> trainersInfoList= response.getMentorsList();
        assertEquals(1, trainersInfoList.size());
        assertEquals(trainerShortInfo, trainersInfoList.get(0));
    }

}

