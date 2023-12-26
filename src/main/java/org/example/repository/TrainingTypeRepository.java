package org.example.repository;

import org.example.domain_entities.TrainingType;

import java.util.List;
import java.util.Optional;

public interface TrainingTypeRepository {
    Optional<TrainingType> get(String name);
    List<TrainingType> getAll();
    TrainingType create(String name);
    //void delete(String name);
}
