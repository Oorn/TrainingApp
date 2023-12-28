package org.example.requests_responses.training;

import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
public class CreateTrainingRequest {
    private String traineeUsername;

    private String trainerUsername;

    private String name;

    private Instant date;

    private Duration duration;

    public CreateTrainingRequest(String traineeUsername, String trainerUsername, String name, String date, String duration) {
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.name = name;
        this.date = Instant.parse(date);
        this.duration = Duration.parse(duration);
    }
}
