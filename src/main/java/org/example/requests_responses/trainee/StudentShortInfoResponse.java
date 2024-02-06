package org.example.requests_responses.trainee;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentShortInfoResponse {
    private String username;

    private String firstName;

    private String lastName;
}
