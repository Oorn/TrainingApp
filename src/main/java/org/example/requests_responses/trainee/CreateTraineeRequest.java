package org.example.requests_responses.trainee;

import lombok.*;

import java.util.Date;


@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateTraineeRequest {

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;
}
