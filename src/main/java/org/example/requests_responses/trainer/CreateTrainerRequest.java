package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
public class CreateTrainerRequest {
    private String firstName;

    private String lastName;

    private String specialisation;

    public CreateTrainerRequest(String firstName, String lastName, String specialisation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialisation = specialisation;
    }
}
