package org.example.service;

import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;

public interface TraineeService {
    CredentialsResponse createTrainee (CreateTraineeRequest request);
    TraineeFullInfoResponse getTraineeProfile(String username);
    TraineeFullInfoResponse updateTraineeProfile(UpdateTraineeProfileRequest request);
    boolean deleteTraineeProfile(String username);

}
