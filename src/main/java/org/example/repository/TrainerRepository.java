package org.example.repository;

import org.example.domain_entities.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Optional<Trainer> getTrainerByUsername(String Username);
    List<Trainer> getAllTrainers();
    Trainer saveTrainer(Trainer entity);
}
