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
public class CreateTrainingForStudentRequest {
    //@Schema(hidden = true)
    //private String traineeUsername;

    //@Schema(hidden = true)
    private String mentorUsername;

    private String name;

    private Timestamp date;

    @Schema(defaultValue = "PT1H30M")
    private Duration duration;

    public CreateTrainingForStudentRequest(String mentorUsername, String name, String date, String duration) {
        this.mentorUsername = mentorUsername;
        this.name = name;
        this.date = Timestamp.from(Instant.parse(date));
        this.duration = Duration.parse(duration);
    }
}
