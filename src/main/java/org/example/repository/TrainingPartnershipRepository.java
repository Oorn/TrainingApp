package org.example.repository;

import org.example.domain_entities.TrainingPartnership;

import java.util.List;

public interface TrainingPartnershipRepository {
    List<TrainingPartnership> getByTraineeName(String username);

    List<TrainingPartnership> getByTrainerName(String username);

    List<TrainingPartnership> updateAndReturnListForTrainee(String username, List<String> trainerUsernames);

    List<TrainingPartnership> updateAndReturnListForTrainer(String username, List<String> traineeUsernames);

    TrainingPartnership save(TrainingPartnership entity);

    void deleteAllForTrainee(String username);

    void deleteAllForTrainer(String username);

}
