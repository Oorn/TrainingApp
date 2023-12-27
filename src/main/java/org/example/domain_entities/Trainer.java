package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trainer {

    @EqualsAndHashCode.Include
    private  long id;

    private TrainingType specialization;

    private User user;

    private boolean isRemoved;

    @ToString.Exclude
    private Set<TrainingPartnership> trainingPartnerships;

}
