package org.example.service;

import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;

public interface TrainerService {
    CredentialsResponse createTrainer(CreateTrainerRequest request);
    TrainerFullInfoResponse getTrainerProfile(String username);
    TrainerFullInfoResponse updateTrainerProfile(UpdateTrainerProfileRequest request);
}
