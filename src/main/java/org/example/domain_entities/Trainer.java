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
@Table(name = "trainers", schema = "training_app")
public class Trainer {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "training_type_fk", nullable = false)
    private TrainingType specialization;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<TrainingPartnership> trainingPartnerships;

}
