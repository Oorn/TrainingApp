package org.example.requests_responses.trainingpartnership;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainingPartnershipListRequest {
    private String username;

    private List<String> trainerUsername;
}
