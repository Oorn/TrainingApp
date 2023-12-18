package org.example.repository.impl;

import org.example.domain_entities.Trainee;
import org.example.repository.TraineeRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {
    @Override
    public Optional<Trainee> getByUsername(String Username) {
        return Optional.empty();
        //todo
    }

    @Override
    public Trainee save(Trainee entity) {
        return null;
        //todo
    }

    @Override
    public void delete(String username) {
        //todo
    }
}
