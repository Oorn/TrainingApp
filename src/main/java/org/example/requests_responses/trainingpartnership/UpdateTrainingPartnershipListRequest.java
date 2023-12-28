package org.example.requests_responses.trainingpartnership;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainingPartnershipListRequest {
    private String username;

    private List<String> trainerUsernames;

    public UpdateTrainingPartnershipListRequest(String username, String... trainerUsernames) {
        this.username = username;
        this.trainerUsernames = Arrays.asList(trainerUsernames);
    }
}
