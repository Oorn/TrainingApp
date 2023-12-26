package org.example.repository;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;

import java.util.List;
import java.util.Optional;

public interface TrainingPartnershipRepository {

    Optional<TrainingPartnership> getByTraineeTrainer(String traineeName, String trainerName);
    List<TrainingPartnership> getByTraineeName(String username);

    List<TrainingPartnership> getByTrainerName(String username);

    List<TrainingPartnership> updateAndReturnListForTrainee(String username, List<Trainer> trainerUsernames);

    List<TrainingPartnership> updateAndReturnListForTrainer(String username, List<Trainee> traineeUsernames);

    TrainingPartnership save(TrainingPartnership entity);

   //void deleteAllForTrainee(String username);

    //void deleteAllForTrainer(String username);

}
