package org.example.converters;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.Training;
import org.example.domain_entities.TrainingPartnership;
import org.example.requests_responses.training.TrainingInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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

        Trainee trainee = Mockito.mock(Trainee.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(trainee.getUser().getUserName()).thenReturn(traineeUsername);

        Trainer trainer = Mockito.mock(Trainer.class, Mockito.RETURNS_DEEP_STUBS);
        Mockito.when(trainer.getUser().getUserName()).thenReturn(trainerUsername);
        Mockito.when(trainer.getSpecialization().getTrainingType()).thenReturn(type);

        Training training = Training.builder()
                .trainingDateFrom(dateFrom)
                .trainingDateTo(dateTo)
                .trainingName(trainingName)
                .trainingPartnership(TrainingPartnership.builder()
                        .trainer(trainer)
                        .trainee(trainee)
                        .build())
                .build();

        TrainingInfoResponse response = converter.convert(training);

        assert response != null;
        assertEquals(dateFrom, response.getDate());
        assertEquals(duration, response.getDuration());
        assertEquals(trainingName, response.getName());
        assertEquals(type, response.getType());
        assertEquals(traineeUsername, response.getTraineeUsername());
        assertEquals(trainerUsername, response.getTrainerUsername());
    }
}