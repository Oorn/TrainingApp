package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrainingPartnership {

    @EqualsAndHashCode.Include
    private long id;

    private Trainer trainer;

    private Trainee trainee;

    private boolean isRemoved;

    private Set<Training> trainings;

}
