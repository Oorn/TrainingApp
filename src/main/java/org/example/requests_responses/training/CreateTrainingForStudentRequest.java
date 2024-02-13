package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.validation.ValidationConstants;
import org.hibernate.validator.constraints.time.DurationMax;
import org.hibernate.validator.constraints.time.DurationMin;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @NotBlank
    @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
    private String mentorUsername;

    @NotBlank
    @Size(min = ValidationConstants.MIN_TRAINING_NAME_LENGTH, max = ValidationConstants.MAX_TRAINING_NAME_LENGTH)
    private String name;

    @NotNull
    @Future
    private Timestamp date;

    @Schema(defaultValue = "PT1H30M")
    @NotNull
    @DurationMin(seconds = ValidationConstants.MIN_TRAINING_DURATION_SECONDS)
    @DurationMax(seconds = ValidationConstants.MAX_TRAINING_DURATION_SECONDS)
    private Duration duration;

    public CreateTrainingForStudentRequest(String mentorUsername, String name, String date, String duration) {
        this.mentorUsername = mentorUsername;
        this.name = name;
        this.date = Timestamp.from(Instant.parse(date));
        this.duration = Duration.parse(duration);
    }
}
