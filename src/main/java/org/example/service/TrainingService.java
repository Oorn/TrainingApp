package org.example.service;

import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;

public interface TrainingService {
    boolean addTraining(CreateTrainingRequest request);
    MultipleTrainingInfoResponse getTraineeTrainings(GetTraineeTrainingsRequest request);
    MultipleTrainingInfoResponse getTrainerTrainings(GetTrainerTrainingsRequest request);
}
