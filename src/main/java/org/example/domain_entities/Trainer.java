package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
public class Trainer {

    private  long id;

    private TrainingType specialization;

    private User user;

    private boolean isRemoved;

    private Set<TrainingPartnership> trainingPartnerships;

}
