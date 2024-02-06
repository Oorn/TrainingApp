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
@Table(name = "training_types", schema = "training_app")
public class Specialisation {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "training_type_name")
    private String specialisationName;

    @ToString.Exclude
    @OneToMany(mappedBy = "specialization", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Set<Mentor> mentors;
}
