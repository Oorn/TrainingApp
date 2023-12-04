package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class TrainingPartnership {

    private  Long id;

    private Trainer trainer;

    private Trainee trainee;

    private Boolean isRemoved = false;

    private Set<Training> trainings;

}
