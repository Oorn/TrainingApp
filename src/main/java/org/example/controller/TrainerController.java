package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.example.requests_responses.trainer.UpdateTrainerProfileRequest;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainer")
public class TrainerController {
    @Setter(onMethod_={@Autowired})
    private TrainerService trainerService;
    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @GetMapping
    @Operation(summary = "trainer info")
    public ResponseEntity<Object> getTrainer(@RequestParam String username){
        TrainerFullInfoResponse result = trainerService.get(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @Operation(summary = "update trainer")
    public ResponseEntity<Object> updateTrainer(@RequestBody UpdateTrainerProfileRequest request) {
        TrainerFullInfoResponse result = trainerService.update(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/get-trainings")
    @Operation(summary = "associated trainings")
    public ResponseEntity<Object> getTrainings(@RequestBody GetTrainerTrainingsRequest request){
        MultipleTrainingInfoResponse result = trainingService.getByTrainer(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
