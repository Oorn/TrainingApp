package org.example.domain_entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_partnerships", schema = "training_app")
public class TrainingPartnership {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_fk", nullable = false)
    private Trainer trainer;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "trainee_fk", nullable = false)
    private Trainee trainee;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @OneToMany(mappedBy = "trainingPartnership", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Training> trainings;

}
