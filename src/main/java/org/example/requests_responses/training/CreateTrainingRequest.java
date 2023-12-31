package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingRequest {
    @Schema(hidden = true)
    private String traineeUsername;

    @Schema(hidden = true)
    private String trainerUsername;

    private String name;

    private Instant date;

    @Schema(defaultValue = "PT1H30M")
    private Duration duration;

    public CreateTrainingRequest(String traineeUsername, String trainerUsername, String name, String date, String duration) {
        this.traineeUsername = traineeUsername;
        this.trainerUsername = trainerUsername;
        this.name = name;
        this.date = Instant.parse(date);
        this.duration = Duration.parse(duration);
    }
}
