package org.example.requests_responses.trainee;

import lombok.*;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TraineeFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private Instant dateOfBirth;

    private String address;

    private boolean isActive;

    private List<TrainerShortInfoResponse> trainersList;

}
