package org.example.repository;

import org.example.domain_entities.Training;
import org.example.repository.dto.TrainingSearchFilter;

import java.util.List;

@Deprecated
public interface TrainingRepository {
    Training save(Training training);
    List<Training> getTrainingsByFilter(TrainingSearchFilter searchFilter);

    //void delete(Training training);
}
