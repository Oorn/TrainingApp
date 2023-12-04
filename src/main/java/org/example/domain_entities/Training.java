package org.example.domain_entities;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@Builder
public class Training {

    private Long id;

    private TrainingPartnership trainingPartnership;

    private String trainingName;

    private Date trainingDateFrom;

    private Date trainingDateTo;

    private Boolean isRemoved = false;
}
