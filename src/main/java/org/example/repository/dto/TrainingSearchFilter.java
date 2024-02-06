package org.example.repository.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class TrainingSearchFilter {
    private String studentName;
    private String mentorName;
    private String specialisation;
    private Timestamp dateFrom;
    private Timestamp dateTo;
}
