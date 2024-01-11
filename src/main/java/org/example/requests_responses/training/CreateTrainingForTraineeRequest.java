package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateTrainingForTraineeRequest {
    //@Schema(hidden = true)
    //private String traineeUsername;

    //@Schema(hidden = true)
    private String trainerUsername;

    private String name;

    private Instant date;

    @Schema(defaultValue = "PT1H30M")
    private Duration duration;

    public CreateTrainingForTraineeRequest(String trainerUsername, String name, String date, String duration) {
        this.trainerUsername = trainerUsername;
        this.name = name;
        this.date = Instant.parse(date);
        this.duration = Duration.parse(duration);
    }
}
