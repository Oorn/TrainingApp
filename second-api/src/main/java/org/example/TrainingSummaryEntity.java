package org.example;

import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "training_summaries")
public class TrainingSummaryEntity {

    @Id
    private String id;

    //@Indexed
    private  String username;

    //@Indexed
    private String firstName;

    //@Indexed
    private String lastName;

    private boolean isActive;

    //@Indexed
    private LocalDateTime month;

    private long duration;

}
