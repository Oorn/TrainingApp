package org.example.converters;

import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetStudentTrainingsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetStudentTrainingsRequestConverterTest {

    GetStudentTrainingsRequestConverter converter = new GetStudentTrainingsRequestConverter();

    @Test
    void convert() {
        String trainer = "trainer";
        String type = "type";
        Instant dateFrom = Instant.parse("2018-11-30T18:35:24.00Z");
        Instant dateTo = Instant.parse("2018-11-30T20:35:24.00Z");

        GetStudentTrainingsRequest request = GetStudentTrainingsRequest.builder()
                .dateFrom(Timestamp.from(dateFrom))
                .dateTo(Timestamp.from(dateTo))
                .mentorUsername(trainer)
                .type(type)
                .build();

        TrainingSearchFilter filter = converter.convert(request);
        assert filter != null;
        assertEquals(trainer, filter.getMentorName());
        assertEquals(type, filter.getSpecialisation());
        assertEquals(dateFrom, filter.getDateFrom().toInstant());
        assertEquals(dateTo, filter.getDateTo().toInstant());
        assertNull(filter.getStudentName());
    }
}