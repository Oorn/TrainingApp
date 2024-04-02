package org.example;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "training_summary")
public class TrainingSummaryEntity {

    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  long id;

    @Column(name = "username")
    private  String username;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "month_start")
    private Timestamp month;

    @Column(name = "duration")
    private long duration;

}
