package org.example.requests_responses.trainee;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class UpdateTraineeProfileRequest {

    private String username;

    private String firstName;

    private String lastName;

    private Instant dateOfBirth;

    private String address;

    private boolean isActive;
}
