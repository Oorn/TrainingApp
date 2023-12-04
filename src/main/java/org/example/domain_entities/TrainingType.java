package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class TrainingType {

    private  Long id;

    private String trainingType;

    private Boolean isRemoved = false;

    private Set<Trainer> trainers;
}
