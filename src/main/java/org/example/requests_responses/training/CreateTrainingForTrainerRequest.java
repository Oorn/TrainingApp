package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingForTrainerRequest {

    private String traineeUsername;

    private String name;

    private Timestamp date;

    @Schema(defaultValue = "PT1H30M")
    private Duration duration;

    public CreateTrainingForTrainerRequest(String traineeUsername, String name, String date, String duration) {
        this.traineeUsername = traineeUsername;
        this.name = name;
        this.date = Timestamp.from(Instant.parse(date));
        this.duration = Duration.parse(duration);
    }
}