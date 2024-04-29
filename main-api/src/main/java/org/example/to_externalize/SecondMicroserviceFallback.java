package org.example.to_externalize;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.requests_responses.training.TrainingDurationSummaryRequest;
import org.example.requests_responses.training.TrainingDurationSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SecondMicroserviceFallback implements SecondMicroservice{

    @Autowired
    @Lazy
    SecondMicroserviceWrapper wrapper;

    @Override
    public String ping(String jwt) {
        return "second service not pong";
    }

    @Override
    public void putTraining(String jwt, SecondMicroservicePutTrainingRequest req) {
        wrapper.resetToken();
    }

    @Override
    public TrainingDurationSummaryResponse getTrainingSummary(String jwt, TrainingDurationSummaryRequest request, String username) {
        wrapper.resetToken();
        return TrainingDurationSummaryResponse.builder()
                .executionStatus(TrainingDurationSummaryResponse.STATUS_SERVICE_DOWN)
                .username(username)
                .name("")
                .surname("")
                .duration(Duration.ZERO)
                .active(false)
                .build();
    }

    @Override
    public String login(String password, String username) {
        wrapper.resetToken();
        return null;
    }
}
