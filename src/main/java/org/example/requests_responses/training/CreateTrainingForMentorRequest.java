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
public class CreateTrainingForMentorRequest {

    private String studentUsername;

    private String name;

    private Timestamp date;

    @Schema(defaultValue = "PT1H30M")
    private Duration duration;

    public CreateTrainingForMentorRequest(String studentUsername, String name, String date, String duration) {
        this.studentUsername = studentUsername;
        this.name = name;
        this.date = Timestamp.from(Instant.parse(date));
        this.duration = Duration.parse(duration);
    }
}