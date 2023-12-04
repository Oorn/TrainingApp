package org.example.repository;

import org.example.domain_entities.TrainingPartnership;

import java.util.List;

public interface TrainingPartnershipRepository {
    List<TrainingPartnership> getTrainingPartnershipsByTraineeName(String username);

    List<TrainingPartnership> getTrainingPartnershipsByTrainerName(String username);

    List<TrainingPartnership> saveTrainingPartnerships(List<TrainingPartnership> partnerships);


}
