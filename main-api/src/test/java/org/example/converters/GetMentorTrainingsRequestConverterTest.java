package org.example.converters;

import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetMentorTrainingsRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class GetMentorTrainingsRequestConverterTest {

    GetMentorTrainingsRequestConverter converter = new GetMentorTrainingsRequestConverter();

    @Test
    void convert() {
        String trainee = "trainee";
        Instant dateFrom = Instant.parse("2018-11-30T18:35:24.00Z");
        Instant dateTo = Instant.parse("2018-11-30T20:35:24.00Z");

        GetMentorTrainingsRequest request = GetMentorTrainingsRequest.builder()
                .dateFrom(Timestamp.from(dateFrom))
                .dateTo(Timestamp.from(dateTo))
                .studentUsername(trainee)
                .build();

        TrainingSearchFilter filter = converter.convert(request);
        assert filter != null;
        assertEquals(trainee, filter.getStudentName());
        assertNull(filter.getSpecialisation());
        assertEquals(dateFrom, filter.getDateFrom().toInstant());
        assertEquals(dateTo, filter.getDateTo().toInstant());
        assertNull(filter.getMentorName());
    }
}