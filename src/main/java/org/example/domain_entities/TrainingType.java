package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TrainingType {

    @EqualsAndHashCode.Include
    private long id;

    private String trainingType;

    private boolean isRemoved;

    private Set<Trainer> trainers;
}
