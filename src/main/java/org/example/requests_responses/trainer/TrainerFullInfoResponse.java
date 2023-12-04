package org.example.requests_responses.trainer;

import lombok.*;
import org.example.requests_responses.trainee.TraineeShortInfoResponse;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TrainerFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private String specialization;

    private boolean isActive;

    List<TraineeShortInfoResponse> traineesList;

}
