package org.example.repository;

import org.example.domain_entities.TrainingPartnership;

import java.util.List;

public interface TrainingPartnershipRepository {
    List<TrainingPartnership> getByTraineeName(String username);

    List<TrainingPartnership> getByTrainerName(String username);

    List<TrainingPartnership> save(List<TrainingPartnership> partnerships);


}
