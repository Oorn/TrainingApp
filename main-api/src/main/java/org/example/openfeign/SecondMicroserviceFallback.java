package org.example.openfeign;

import org.example.openfeign.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.requests_responses.training.TrainingDurationSummaryRequest;
import org.example.requests_responses.training.TrainingDurationSummaryResponse;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SecondMicroserviceFallback implements SecondMicroservice{
    @Override
    public String ping() {
        return "second service not pong";
    }

    @Override
    public void putTraining(SecondMicroservicePutTrainingRequest req) {
    }

    @Override
    public TrainingDurationSummaryResponse getTrainingSummary(TrainingDurationSummaryRequest request, String username) {
        return TrainingDurationSummaryResponse.builder()
                .executionStatus(TrainingDurationSummaryResponse.STATUS_SERVICE_DOWN)
                .username(username)
                .name("")
                .surname("")
                .duration(Duration.ZERO)
                .active(false)
                .build();
    }
}
