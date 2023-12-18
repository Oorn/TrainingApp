package org.example.repository;

import org.example.domain_entities.Training;
import org.example.repository.dto.TrainingSearchFilter;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    Training save(Training training);
    List<Training> getTrainingsByCriteria(TrainingSearchFilter searchFilter);
}
