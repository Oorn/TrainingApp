package org.example.converters;

import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetTrainerTrainingsRequestConverterTest {

    GetTrainerTrainingsRequestConverter converter = new GetTrainerTrainingsRequestConverter();

    @Test
    void convert() {
        String trainee = "trainee";
        Instant dateFrom = Instant.parse("2018-11-30T18:35:24.00Z");
        Instant dateTo = Instant.parse("2018-11-30T20:35:24.00Z");

        GetTrainerTrainingsRequest request = GetTrainerTrainingsRequest.builder()
                .dateFrom(dateFrom)
                .dateTo(dateTo)
                .traineeUsername(trainee)
                .build();

        TrainingSearchFilter filter = converter.convert(request);
        assert filter != null;
        assertEquals(trainee, filter.getTraineeName());
        assertNull(filter.getTrainingType());
        assertEquals(dateFrom, filter.getDateFrom());
        assertEquals(dateTo, filter.getDateTo());
        assertNull(filter.getTrainerName());
    }
}