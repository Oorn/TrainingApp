package org.example.requests_responses.trainee;

import lombok.*;

import java.util.Date;


@Data
@Builder
public class CreateTraineeRequest {

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;
}
