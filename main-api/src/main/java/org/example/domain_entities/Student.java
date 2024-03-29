package org.example.domain_entities;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


@Builder
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trainees", schema = "training_app")
public class Student {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(name = "date_of_birth")
    private Timestamp dateOfBirth;

    @Column(name = "address")
    private String address;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_fk", referencedColumnName = "id", nullable = false)
    private User user;

    @Column(name = "is_removed")
    private boolean isRemoved;

    @OneToMany(mappedBy = "student", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @ToString.Exclude
    private Set<Partnership> partnerships;

}
