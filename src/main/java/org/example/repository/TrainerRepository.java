package org.example.repository;

import org.example.domain_entities.Trainer;

import java.util.List;
import java.util.Optional;

@Deprecated
public interface TrainerRepository {
    Optional<Trainer> get(String username);
    List<Trainer> getAll();
    Trainer save(Trainer entity);

    //void delete(String username);

}
