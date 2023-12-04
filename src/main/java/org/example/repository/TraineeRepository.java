package org.example.repository;

import org.example.domain_entities.Trainee;

import java.util.Optional;

public interface TraineeRepository {
    Optional<Trainee> getTraineeByUsername(String Username);
    Trainee saveTrainee(Trainee entity);
    void deleteTrainee(String username);
}
