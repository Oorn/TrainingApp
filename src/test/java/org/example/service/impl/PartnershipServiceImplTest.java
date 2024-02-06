package org.example.service.impl;

import org.example.domain_entities.Student;
import org.example.domain_entities.Mentor;
import org.example.domain_entities.Partnership;
import org.example.repository.StudentHibernateRepository;
import org.example.repository.MentorHibernateRepository;
import org.example.repository.PartnershipHibernateRepository;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PartnershipServiceImplTest {

    @Mock
    private MentorHibernateRepository trainerRepository;

    @Mock
    private StudentHibernateRepository traineeRepository;

    @Mock
    private PartnershipHibernateRepository trainingPartnershipRepository;

    @Mock
    private ConversionService converter;

    @InjectMocks
    private PartnershipServiceImpl service;

    @Test
    void getNotAssignedTrainers() {
        String traineeName = "trainee";
        Student student = mock(Student.class);

        Function<MentorShortInfoResponse, Mentor> trainerMockFactory = (res) -> {
            Mentor mentor = mock(Mentor.class, Answers.RETURNS_DEEP_STUBS);
            when(converter.convert(mentor, MentorShortInfoResponse.class)).thenReturn(res);
            when(mentor.isRemoved()).thenReturn(false);
            when(mentor.getUser().isActive()).thenReturn(true);
            return mentor;
        };

        MentorShortInfoResponse trPresent1res = new MentorShortInfoResponse()
                , trPresent2res = new MentorShortInfoResponse()
                , trAbsent1res = new MentorShortInfoResponse()
                , trAbsent2res = new MentorShortInfoResponse();

        Mentor trPresent1 = trainerMockFactory.apply(trPresent1res)
                , trPresent2 = trainerMockFactory.apply(trPresent2res)
                , trAbsent1 = trainerMockFactory.apply(trAbsent1res)
                , trAbsent2 = trainerMockFactory.apply(trAbsent2res);

        Partnership trPart1 = Partnership.builder()
                .isRemoved(false)
                .student(student)
                .mentor(trPresent1)
                .build();
        Partnership trPart2 = Partnership.builder()
                .isRemoved(false)
                .student(student)
                .mentor(trPresent2)
                .build();

        when(traineeRepository.findStudentByUsername(traineeName)).thenReturn(Optional.of(student));
        when(trainingPartnershipRepository.findPartnershipByTraineeUsername(traineeName)).thenReturn(List.of(trPart1, trPart2));
        when(trainerRepository.findAll()).thenReturn(List.of(trPresent1, trPresent2, trAbsent1, trAbsent2));

        AvailableTrainersResponse response = service.getNotAssignedMentors(traineeName);

        verify(traineeRepository, times(1)).findStudentByUsername(traineeName);
        verify(trainingPartnershipRepository, times(1)).findPartnershipByTraineeUsername(traineeName);
        verify(trainerRepository, times(1)).findAll();
        assert response.getMentors().contains(trAbsent1res);
        assert response.getMentors().contains(trAbsent2res);
        assertEquals(2, response.getMentors().size());
    }

    @Test
    void updateTraineeTrainerList() {

        String traineeName = "trainee";
        Student student = mock(Student.class, Answers.RETURNS_DEEP_STUBS);
        when(student.getUser().isActive()).thenReturn(true);

        BiFunction<String, MentorShortInfoResponse, Mentor> trainerMockFactory = (name, res) -> {
            Mentor mentor = mock(Mentor.class, Answers.RETURNS_DEEP_STUBS);
            when(trainerRepository.findMentorByUsername(name)).thenReturn(Optional.of(mentor));
            when(converter.convert(mentor, MentorShortInfoResponse.class)).thenReturn(res);
            when(mentor.isRemoved()).thenReturn(false);
            when(mentor.getUser().isActive()).thenReturn(true);
            when(mentor.getUser().getUserName()).thenReturn(name);
            return mentor;
        };
        BiFunction<Student, Mentor, Partnership> partnershipFactory = (traineeParam, trainerParam) -> Partnership.builder()
                .mentor(trainerParam)
                .student(traineeParam)
                .isRemoved(false)
                .build();

        MentorShortInfoResponse trPresentPresentRes = new MentorShortInfoResponse()
                , trPresentAbsentRes = new MentorShortInfoResponse()
                , trAbsentPresentRes = new MentorShortInfoResponse();
        final String trPresentPresentName = "trainer1", trPresentAbsentName = "trainer2", trAbsentPresentName = "trainer3";
        Mentor trPresentPresent = trainerMockFactory.apply(trPresentPresentName, trPresentPresentRes)
                , trPresentAbsent = trainerMockFactory.apply(trPresentAbsentName, trPresentAbsentRes)
                , trAbsentPresent = trainerMockFactory.apply(trAbsentPresentName, trAbsentPresentRes);
        Partnership trPartPresentPresent = partnershipFactory.apply(student, trPresentPresent)
                , trPartPresentAbsent = partnershipFactory.apply(student, trPresentAbsent);

        when(traineeRepository.findStudentByUsername(traineeName)).thenReturn(Optional.of(student));
        when(trainingPartnershipRepository.findPartnershipByTraineeUsername(traineeName)).thenReturn(List.of(trPartPresentPresent, trPartPresentAbsent));

        UpdateTrainingPartnershipListRequest request = new UpdateTrainingPartnershipListRequest();
        request.setMentorUsernames(List.of(trPresentPresentName, trAbsentPresentName));
        UpdateTrainingPartnershipListResponse response = service.updateStudentMentorsList(traineeName, request);

        verify(traineeRepository, times(1)).findStudentByUsername(traineeName);
        verify(trainingPartnershipRepository, times(2)).findPartnershipByTraineeUsername(traineeName);
        verify(trainingPartnershipRepository, times(2)).saveAndFlush(argThat((trp) -> {
            switch (trp.getMentor().getUser().getUserName()) {
                case trPresentPresentName:
                case trAbsentPresentName:
                    assertFalse(trp.isRemoved());
                    break;
                case trPresentAbsentName:
                    assertTrue(trp.isRemoved());
                    break;
                default:
                    return false;
            }
            return true;
        }));
    }
}