package org.example.converters;

import org.example.domain_entities.Student;
import org.example.domain_entities.Mentor;
import org.example.domain_entities.Training;
import org.example.domain_entities.Partnership;
import org.example.requests_responses.training.TrainingInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainingInfoResponseConverterTest {

    TrainingInfoResponseConverter converter = new TrainingInfoResponseConverter();

    @Test
    void convert() {
        String traineeUsername = "trainee", trainerUsername = "trainer", type = "type", trainingName = "training";
        Instant dateFrom = Instant.parse("2018-11-30T18:35:24.00Z");
        Instant dateTo = Instant.parse("2018-11-30T20:35:24.00Z");
        Duration duration = Duration.between(dateFrom, dateTo);

        Student student = Mockito.mock(Student.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(student.getUser().getUserName()).thenReturn(traineeUsername);

        Mentor mentor = Mockito.mock(Mentor.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(mentor.getUser().getUserName()).thenReturn(trainerUsername);
        Mockito.when(mentor.getSpecialization().getSpecialisationName()).thenReturn(type);

        Training training = Training.builder()
                .trainingDateFrom(Timestamp.from(dateFrom))
                .trainingDateTo(Timestamp.from(dateTo))
                .trainingName(trainingName)
                .partnership(Partnership.builder()
                        .mentor(mentor)
                        .student(student)
                        .build())
                .build();

        TrainingInfoResponse response = converter.convert(training);

        assert response != null;
        assertEquals(dateFrom, response.getDate().toInstant());
        assertEquals(duration, response.getDuration());
        assertEquals(trainingName, response.getName());
        assertEquals(type, response.getType());
        assertEquals(traineeUsername, response.getStudentUsername());
        assertEquals(trainerUsername, response.getMentorUsername());
    }
}