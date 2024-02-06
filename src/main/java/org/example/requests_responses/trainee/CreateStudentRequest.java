package org.example.requests_responses.trainee;

import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateStudentRequest {

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private String address;


    public CreateStudentRequest(String firstName, String lastName, String dateOfBirth, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = Timestamp.from(Instant.parse(dateOfBirth));
        this.address = address;
    }
}
