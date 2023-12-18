package org.example.requests_responses.training;

import lombok.*;

import java.time.Duration;
import java.util.Date;

@Data
@Builder
public class CreateTrainingRequest {
    private String traineeUsername;

    private String trainerUsername;

    private String name;

    private Date date;

    private String type;

    private Duration duration;
}
