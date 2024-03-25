package org.example.converters;

import org.example.domain_entities.*;
import org.example.requests_responses.trainee.StudentShortInfoResponse;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MentorFullInfoResponseConverterTest {

    @Mock
    private ConversionService conversionService;
    @InjectMocks
    MentorFullInfoResponseConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String userName = "username";
        String type = "type";
        Specialisation specialisation = Specialisation.builder().specialisationName(type).build();

        boolean isRemoved = false;
        boolean isActive = true;
        Set<Partnership> partnerships = new HashSet<>();
        StudentShortInfoResponse traineeShortInfo = new StudentShortInfoResponse();

        User user = User.builder()
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .isRemoved(isRemoved)
                .isActive(isActive)
                .build();
        Mentor mentor = Mentor.builder()
                .isRemoved(isRemoved)
                .specialization(specialisation)
                .partnerships(partnerships)
                .user(user)
                .build();
        user.setMentorProfile(mentor);

        Student removedPartnershipStudent = Student.builder().build();

        User traineeUser = Mockito.mock(User.class);
        Student student = Student.builder().user(traineeUser).build();
        Mockito.when(traineeUser.isActive()).thenReturn(true);

        User inactiveTraineeUser = Mockito.mock(User.class);
        Student inactiveStudent = Student.builder().user(inactiveTraineeUser).build();
        Mockito.when(inactiveTraineeUser.isActive()).thenReturn(false);

        partnerships.add(Partnership.builder().id(1).isRemoved(true).mentor(mentor).student(removedPartnershipStudent).build());
        partnerships.add(Partnership.builder().id(2).isRemoved(false).mentor(mentor).student(inactiveStudent).build());
        partnerships.add(Partnership.builder().id(3).isRemoved(false).mentor(mentor).student(student).build());

        Mockito.when(conversionService.convert(student, StudentShortInfoResponse.class)).thenReturn(traineeShortInfo);

        MentorFullInfoResponse response = converter.convert(mentor);

        assert response != null;
        assertEquals(type, response.getSpecialization());
        assertEquals(firstName, response.getFirstName());
        assertEquals(isActive, response.isActive());
        assertEquals(lastName, response.getLastName());
        assertEquals(userName, response.getUsername());
        List<StudentShortInfoResponse> traineesInfoList= response.getStudentsList();
        assertEquals(1, traineesInfoList.size());
        assertEquals(traineeShortInfo, traineesInfoList.get(0));
    }

}