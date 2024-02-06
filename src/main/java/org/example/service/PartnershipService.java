package org.example.service;

import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;

public interface PartnershipService {
    AvailableTrainersResponse getNotAssignedMentors(String traineeUsername);
    UpdateTrainingPartnershipListResponse updateStudentMentorsList(String authUsername, UpdateTrainingPartnershipListRequest request);
}
