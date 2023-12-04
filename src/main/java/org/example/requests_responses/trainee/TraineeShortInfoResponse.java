package org.example.requests_responses.trainee;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraineeShortInfoResponse {
    private String username;

    private String firstName;

    private String lastName;
}
