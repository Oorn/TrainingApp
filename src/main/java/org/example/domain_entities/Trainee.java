package org.example.domain_entities;

import lombok.*;

import java.time.Instant;
import java.util.Date;
import java.util.Set;


@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trainee {

    @EqualsAndHashCode.Include
    private  long id;

    private Instant dateOfBirth;

    private String Address;

    private User user;

    private boolean isRemoved;

    private Set<TrainingPartnership> trainingPartnerships;

}
