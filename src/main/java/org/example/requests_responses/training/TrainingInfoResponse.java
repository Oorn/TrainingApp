package org.example.requests_responses.training;

import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingInfoResponse {
    private String studentUsername;

    private String mentorUsername;

    private String name;

    private String type;

    private Timestamp date;

    private Duration duration;
}
