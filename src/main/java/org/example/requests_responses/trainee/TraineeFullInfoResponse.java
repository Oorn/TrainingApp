package org.example.requests_responses.trainee;

import lombok.*;

import java.util.Date;

@Data
@Builder
public class TraineeFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;

}
