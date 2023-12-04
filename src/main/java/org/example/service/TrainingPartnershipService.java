package org.example.service;

import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;

public interface TrainingPartnershipService {
    AvailableTrainersResponse getNotAssignedTrainers (String traineeUsername);
    UpdateTrainingPartnershipListResponse updateTraineeTrainerList(UpdateTrainingPartnershipListRequest request);
}
