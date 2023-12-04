package org.example.requests_responses.training;

import lombok.*;

import java.util.List;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MultipleTrainingInfoResponse {
    private List<TrainingInfoResponse> trainings;
}
