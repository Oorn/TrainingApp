package org.example.requests_responses.training;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class GetTrainerTrainingsRequest {
    private String username;

    private String traineeUsername;

    private Instant dateFrom;

    private Instant dateTo;
}
