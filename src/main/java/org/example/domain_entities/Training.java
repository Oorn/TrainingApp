package org.example.domain_entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainings", schema = "training_app")
public class Training {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinColumn(name = "training_partnership_fk", nullable = false)
    private TrainingPartnership trainingPartnership;

    @Column(name = "training_name")
    private String trainingName;

    @Column(name = "timestamp_from")
    private Timestamp trainingDateFrom;

    @Column(name = "timestamp_to")
    private Timestamp trainingDateTo;

    @Column(name = "is_removed")
    private boolean isRemoved;
}
