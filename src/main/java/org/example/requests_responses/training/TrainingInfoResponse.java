package org.example.requests_responses.training;

import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingInfoResponse {
    private String traineeUsername;

    private String trainerUsername;

    private String name;

    private String type;

    private Timestamp date;

    private Duration duration;
}
