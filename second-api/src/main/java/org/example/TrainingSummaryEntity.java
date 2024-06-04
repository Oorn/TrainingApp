package org.example;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "training_summaries")
public class TrainingSummaryEntity {

    @EqualsAndHashCode.Include
    @MongoId
    private  long id;

    @Indexed(unique = true)
    private  String username;

    @Indexed
    private String firstName;

    @Indexed
    private String lastName;

    private boolean isActive;

    private Timestamp month;

    private long duration;

}
