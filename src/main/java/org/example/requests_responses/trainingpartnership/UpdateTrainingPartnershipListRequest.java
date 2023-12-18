package org.example.requests_responses.trainingpartnership;

import lombok.*;

import java.util.List;

@Data
@Builder
public class UpdateTrainingPartnershipListRequest {
    private String username;

    private List<String> trainerUsername;
}
