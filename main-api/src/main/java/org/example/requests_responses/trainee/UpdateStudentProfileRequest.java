package org.example.requests_responses.trainee;

import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentProfileRequest {


    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    @NotBlank
    @Size(min = ValidationConstants.MIN_FIRST_NAME_LENGTH, max = ValidationConstants.MAX_FIRST_NAME_LENGTH)
    private String firstName;

    @NotBlank
    @Size(min = ValidationConstants.MIN_LAST_NAME_LENGTH, max = ValidationConstants.MAX_LAST_NAME_LENGTH)
    private String lastName;

    @Past
    private Timestamp dateOfBirth;

    @Size(max = ValidationConstants.MAX_ADDRESS_LENGTH)
    private String address;

    @NotNull
    private boolean isActive;

    public UpdateStudentProfileRequest(String firstName, String lastName, String dateOfBirth, String address, String isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Timestamp.from(Instant.parse(dateOfBirth));
        this.address = address;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
