package org.example.requests_responses.trainingpartnership;

import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainingPartnershipListRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    @NotNull
    private List<@Size(max = ValidationConstants.MAX_USERNAME_LENGTH) String>
            mentorUsernames;

    public UpdateTrainingPartnershipListRequest(String... mentorUsernames) {
        this.mentorUsernames = Arrays.asList(mentorUsernames);
    }
}
