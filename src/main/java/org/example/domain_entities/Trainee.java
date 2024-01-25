package org.example.domain_entities;

import lombok.*;

import javax.persistence.*;
import java.time.Instant;
import java.util.Set;


@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees", schema = "training_app")
public class Trainee {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(name = "date_of_birth")
    private Instant dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @OneToMany(mappedBy = "trainee", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<TrainingPartnership> trainingPartnerships;

}
