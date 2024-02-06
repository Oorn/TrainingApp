package org.example.requests_responses.trainee;

import lombok.*;
import org.example.requests_responses.trainer.CreateTrainerRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTraineeRequest {

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private String address;


    public CreateTraineeRequest(String firstName, String lastName, String dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Timestamp.from(Instant.parse(dateOfBirth));
        this.address = address;
    }
}
