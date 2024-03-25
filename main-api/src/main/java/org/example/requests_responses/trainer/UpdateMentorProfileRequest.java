package org.example.requests_responses.trainer;

import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMentorProfileRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    @NotBlank
    @Size(min = ValidationConstants.MIN_FIRST_NAME_LENGTH, max = ValidationConstants.MAX_FIRST_NAME_LENGTH)
    private String firstName;

    @NotBlank
    @Size(min = ValidationConstants.MIN_LAST_NAME_LENGTH, max = ValidationConstants.MAX_LAST_NAME_LENGTH)
    private String lastName;

    @NotNull
    private boolean isActive;


    public UpdateMentorProfileRequest(String firstName, String lastName, String isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
