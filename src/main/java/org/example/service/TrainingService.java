package org.example.service;

import org.example.requests_responses.training.*;

public interface TrainingService {
    boolean create(String authUsername, CreateTrainingForTraineeRequest request);

    boolean create(String authUsername, CreateTrainingForTrainerRequest request);
    MultipleTrainingInfoResponse getByTrainee(String authUsername, GetTraineeTrainingsRequest request);
    MultipleTrainingInfoResponse getByTrainer(String authUsername, GetTrainerTrainingsRequest request);
}
