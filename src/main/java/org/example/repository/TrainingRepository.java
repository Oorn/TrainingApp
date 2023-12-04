package org.example.repository;

import org.example.domain_entities.Training;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TrainingRepository {
    Training saveTraining(Training training);
    List<Training> getTrainingsByCriteria(Optional<String> traineeName, Optional<String> trainerName, Optional<String> trainingType, Optional<Date> dateFrom, Optional<Date> dateTo);
}
