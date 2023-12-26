package org.example.requests_responses.training;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class GetTraineeTrainingsRequest {
    private String username;

    private String trainerUsername;

    private String type;

    private Instant dateFrom;

    private Instant dateTo;
}
