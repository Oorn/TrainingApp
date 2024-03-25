package org.example.service;

import org.example.requests_responses.training.*;

public interface TrainingService {
    boolean create(String authUsername, CreateTrainingForStudentRequest request);

    boolean create(String authUsername, CreateTrainingForMentorRequest request);
    MultipleTrainingInfoResponse getByStudent(String authUsername, GetStudentTrainingsRequest request);
    MultipleTrainingInfoResponse getByMentor(String authUsername, GetMentorTrainingsRequest request);
}
