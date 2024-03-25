package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorShortInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private String specialization;
}
