package org.example.domain_entities;

import lombok.*;

import java.util.Date;
import java.util.Set;


@Builder
@Data
public class Trainee {

    private  long id;

    private Date dateOfBirth;

    private String Address;

    private User user;

    private boolean isRemoved;

    private Set<TrainingPartnership> trainingPartnerships;

}
