package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
public class TrainingType {

    private long id;

    private String trainingType;

    private boolean isRemoved;

    private Set<Trainer> trainers;
}
