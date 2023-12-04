package org.example.domain_entities;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class User {

    private  Long id;

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String passwordSalt;

    private Boolean isActive;

    private Boolean isRemoved = false;

    private Trainer trainerProfile;

    private Trainee traineeProfile;
}
