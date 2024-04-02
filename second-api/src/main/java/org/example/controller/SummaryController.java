package org.example.controller;

import org.example.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.requests_responses.TrainingDurationSummaryRequest;
import org.example.requests_responses.TrainingDurationSummaryResponse;
import org.example.service.TrainingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class SummaryController {
    @Autowired
    TrainingSummaryService service;

    @PutMapping("/training")
    @Transactional
    void putTraining(@RequestBody SecondMicroservicePutTrainingRequest req){
        service.putTrainingRequest(req);
    }

    @PostMapping("/training_summary/{username}")
    TrainingDurationSummaryResponse getTrainingSummary(@RequestBody TrainingDurationSummaryRequest request,
                                                       @PathVariable(name = "username")  String username){
        return service.getTrainingSummary(request, username);
    }
}
