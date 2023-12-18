package org.example.requests_responses.training;

import lombok.*;

import java.util.Date;

@Data
@Builder
public class GetTrainerTrainingsRequest {
    private String username;

    private String traineeUsername;

    private Date dateFrom;

    private Date dateTo;
}
