package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMentorProfileRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    private String firstName;

    private String lastName;

    private boolean isActive;


    public UpdateMentorProfileRequest(String firstName, String lastName, String isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
