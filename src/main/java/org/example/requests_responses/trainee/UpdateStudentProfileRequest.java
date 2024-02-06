package org.example.requests_responses.trainee;

import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStudentProfileRequest {


    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private String address;

    private boolean isActive;

    public UpdateStudentProfileRequest(String firstName, String lastName, String dateOfBirth, String address, String isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Timestamp.from(Instant.parse(dateOfBirth));
        this.address = address;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
