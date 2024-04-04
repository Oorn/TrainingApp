package org.example.openfeign;


import feign.Headers;
import feign.Param;
import org.example.openfeign.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.requests_responses.training.TrainingDurationSummaryRequest;
import org.example.requests_responses.training.TrainingDurationSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;



@FeignClient(value = "second-api", fallback = SecondMicroserviceFallback.class)
public interface SecondMicroservice {

    @GetMapping("/ping")
    String ping(@RequestHeader("jwt") String jwt);

    @PutMapping("/training")
    void putTraining(@RequestHeader("jwt") String jwt, @RequestBody SecondMicroservicePutTrainingRequest req);

    @PostMapping("/training_summary/{username}")
    TrainingDurationSummaryResponse getTrainingSummary(@RequestHeader("jwt") String jwt, @RequestBody TrainingDurationSummaryRequest request,
                                                       @PathVariable(name = "username")  String username);

    @PostMapping("/login/{username}")
    String login(@RequestBody String password,
                 @PathVariable(name = "username") String username);
}
