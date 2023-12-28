package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class UpdateTrainerProfileRequest {
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
