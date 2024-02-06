package org.example.requests_responses.trainee;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeProfileRequest {


    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //private String username;

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private String address;

    private boolean isActive;

    public UpdateTraineeProfileRequest(String firstName, String lastName, String dateOfBirth, String address, String isActive) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Timestamp.from(Instant.parse(dateOfBirth));
        this.address = address;
        this.isActive = Boolean.parseBoolean(isActive);
    }
}
