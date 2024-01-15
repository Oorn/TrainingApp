package org.example.domain_entities;

import lombok.*;

import java.time.Instant;
import java.util.Set;


@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trainee {

    @EqualsAndHashCode.Include
    private  long id;

    private Instant dateOfBirth;

    private String address;

    private User user;

    private boolean isRemoved;

    @ToString.Exclude
    private Set<TrainingPartnership> trainingPartnerships;

}
