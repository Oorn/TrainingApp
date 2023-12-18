package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
public class CreateTrainerRequest {
    private String firstName;

    private String lastName;

    private String specialisation;

}
