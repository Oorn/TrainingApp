package org.example.requests_responses.trainingpartnership;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainingPartnershipListRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    private List<String> trainerUsernames;

    public UpdateTrainingPartnershipListRequest(String... trainerUsernames) {
        this.trainerUsernames = Arrays.asList(trainerUsernames);
    }
}
