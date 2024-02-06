package org.example.requests_responses.trainingpartnership;

import lombok.*;

import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainingPartnershipListRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    private List<String> mentorUsernames;

    public UpdateTrainingPartnershipListRequest(String... mentorUsernames) {
        this.mentorUsernames = Arrays.asList(mentorUsernames);
    }
}
