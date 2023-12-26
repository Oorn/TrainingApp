package org.example.repository.dto;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Data
@Builder
public class TrainingSearchFilter {
    private String traineeName;
    private String trainerName;
    private String trainingType;
    private Instant dateFrom;
    private Instant dateTo;
}
