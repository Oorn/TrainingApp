package org.example.openfeign;

import org.example.openfeign.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.requests_responses.training.TrainingDurationSummaryRequest;
import org.example.requests_responses.training.TrainingDurationSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@FeignClient(value = "second-api", fallback = SecondMicroserviceFallback.class)
public interface SecondMicroservice {

    @GetMapping("/ping")
    String ping();

    @PutMapping("/training")
    void putTraining(@RequestBody SecondMicroservicePutTrainingRequest req);

    @PostMapping("/training_summary/{username}")
    TrainingDurationSummaryResponse getTrainingSummary(@RequestBody TrainingDurationSummaryRequest request,
                                         @PathVariable(name = "username")  String username);
}
