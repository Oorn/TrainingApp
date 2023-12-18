package org.example.requests_responses.trainingpartnership;

import lombok.*;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;

import java.util.List;

@Data
@Builder
public class UpdateTrainingPartnershipListResponse {
    List<TrainerShortInfoResponse> trainersList;
}
