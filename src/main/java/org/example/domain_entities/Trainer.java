package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Trainer {

    private  Long id;

    private TrainingType specialization;

    private User user;

    private Boolean isRemoved = false;

    private Set<TrainingPartnership> trainingPartnerships;

}
