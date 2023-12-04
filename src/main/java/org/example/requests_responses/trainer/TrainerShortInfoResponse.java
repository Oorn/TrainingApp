package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerShortInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private String specialization;
}
