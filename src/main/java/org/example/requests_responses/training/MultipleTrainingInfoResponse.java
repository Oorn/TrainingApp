package org.example.requests_responses.training;

import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MultipleTrainingInfoResponse {
    private List<TrainingInfoResponse> trainings;
}
