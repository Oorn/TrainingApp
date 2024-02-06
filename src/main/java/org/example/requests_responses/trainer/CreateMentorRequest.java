package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
public class CreateMentorRequest {
    private String firstName;

    private String lastName;

    private String specialisation;

    public CreateMentorRequest(String firstName, String lastName, String specialisation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialisation = specialisation;
    }
}
