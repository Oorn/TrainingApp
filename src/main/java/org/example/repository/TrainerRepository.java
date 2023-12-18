package org.example.repository;

import org.example.domain_entities.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerRepository {
    Optional<Trainer> getByUsername(String Username);
    List<Trainer> getAll();
    Trainer save(Trainer entity);
}
