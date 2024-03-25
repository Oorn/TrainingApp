package org.example.requests_responses.trainer;

import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
public class CreateMentorRequest {
    @NotBlank
    @Size(min = ValidationConstants.MIN_FIRST_NAME_LENGTH, max = ValidationConstants.MAX_FIRST_NAME_LENGTH)
    private String firstName;

    @NotBlank
    @Size(min = ValidationConstants.MIN_LAST_NAME_LENGTH, max = ValidationConstants.MAX_LAST_NAME_LENGTH)
    private String lastName;

    @NotBlank
    private String specialisation;

    public CreateMentorRequest(String firstName, String lastName, String specialisation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialisation = specialisation;
    }
}
