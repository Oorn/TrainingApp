package org.example.requests_responses.training;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainingInfoResponse {
    private String traineeUsername;

    private String trainerUsername;

    private String name;

    private String type;

    private Date date;
}
