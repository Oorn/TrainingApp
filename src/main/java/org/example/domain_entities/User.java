package org.example.domain_entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", schema = "training_app")
public class User {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @EqualsAndHashCode.Include
    @Column(name = "username")
    private String userName;

    @Column(name = "password_hash")
    @JsonIgnore
    private String password;

    @Column(name = "password_salt")
    @JsonIgnore
    private String passwordSalt;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Trainer trainerProfile;

    @ToString.Exclude
    @JsonIgnore
    @OneToOne(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Trainee traineeProfile;
}
