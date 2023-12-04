package org.example.domain_entities;

import lombok.*;

import java.util.Date;
import java.util.Set;


@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Trainee {

    private  Long id;

    private Date dateOfBirth;

    private String Address;

    private User user;

    private Boolean isRemoved = false;

    private Set<TrainingPartnership> trainingPartnerships;

}
