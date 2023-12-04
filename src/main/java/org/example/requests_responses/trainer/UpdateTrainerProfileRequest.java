package org.example.requests_responses.trainer;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrainerProfileRequest {
    private String Username;

    private String firstName;

    private String lastName;

    private String specialisation;

    private boolean isActive;
}
