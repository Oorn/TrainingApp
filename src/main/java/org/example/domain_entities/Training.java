package org.example.domain_entities;

import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
public class Training {

    private long id;

    private TrainingPartnership trainingPartnership;

    private String trainingName;

    private Instant trainingDateFrom;

    private Instant trainingDateTo;

    private boolean isRemoved;
}
