package org.example.repository;

import org.example.domain_entities.Trainee;

import java.util.Optional;

public interface TraineeRepository {
    Optional<Trainee> get(String username);
    Trainee save(Trainee entity);
    void delete(String username);

}
