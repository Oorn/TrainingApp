package org.example.service;

import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;

public interface TrainingService {
    boolean create(CreateTrainingRequest request);
    MultipleTrainingInfoResponse getByTrainee(GetTraineeTrainingsRequest request);
    MultipleTrainingInfoResponse getByTrainer(GetTrainerTrainingsRequest request);
}
