package org.example.requests_responses.trainer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerProfileRequest {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;


    public UpdateTrainerProfileRequest(String username, String firstName, String lastName, String isActive) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
