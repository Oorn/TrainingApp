package org.example.requests_responses.trainee;

import lombok.*;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;

import java.util.Date;
import java.util.List;

@Data
@Builder
public class TraineeFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private Date dateOfBirth;

    private String address;

    private List<TrainerShortInfoResponse> trainersList;

}
