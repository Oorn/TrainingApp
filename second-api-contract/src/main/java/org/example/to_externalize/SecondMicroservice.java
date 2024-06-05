package org.example.to_externalize;



import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;


@FeignClient(value = "second-api", fallback = SecondMicroserviceFallback.class)
public interface SecondMicroservice {

    @GetMapping("/ping")
    String ping(@RequestHeader("jwt") String jwt, @RequestHeader("logging-uuid") String uuid);

    @PutMapping("/training")
    void putTraining(@RequestHeader("jwt") String jwt, @RequestHeader("logging-uuid") String uuid, @RequestBody SecondMicroservicePutTrainingRequest req);

    @PostMapping("/training_summary/{username}")
    TrainingDurationSummaryResponse getTrainingSummary(@RequestHeader("jwt") String jwt, @RequestHeader("logging-uuid") String uuid, @RequestBody TrainingDurationSummaryRequest request,
                                                       @PathVariable(name = "username")  String username);

    @GetMapping("/training_summary_count")
    Long getTrainingSummaryCount(@RequestHeader("jwt") String jwt, @RequestHeader("logging-uuid") String uuid);

    @PostMapping("/login/{username}")
    String login(@RequestHeader("logging-uuid") String uuid,
                 @RequestBody String password,
                 @PathVariable(name = "username") String username);
}
