package org.example.requests_responses.training;

import lombok.*;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetTraineeTrainingsRequest {
    private String username;

    private String trainerUsername;

    private String type;

    private Date dateFrom;

    private Date dateTo;
}
