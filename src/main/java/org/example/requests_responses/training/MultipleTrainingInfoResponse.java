package org.example.requests_responses.training;

import lombok.*;

import java.util.List;

@Data
@Builder
public class MultipleTrainingInfoResponse {
    private List<TrainingInfoResponse> trainings;
}
