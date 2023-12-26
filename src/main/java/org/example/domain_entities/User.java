package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {

    @EqualsAndHashCode.Include
    private long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String passwordSalt;

    private boolean isActive;

    private boolean isRemoved;

    private Trainer trainerProfile;

    private Trainee traineeProfile;
}
