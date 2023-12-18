package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Data
@Builder
public class User {

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
