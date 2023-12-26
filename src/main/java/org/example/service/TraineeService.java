package org.example.service;

import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;

public interface TraineeService {
    CredentialsResponse create (CreateTraineeRequest request);
    TraineeFullInfoResponse get(String username);
    TraineeFullInfoResponse update(UpdateTraineeProfileRequest request);
    boolean delete(String username);

}
