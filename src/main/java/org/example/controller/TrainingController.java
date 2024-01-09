package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.Setter;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

@RestController
public class TrainingController {
    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @PostMapping("/trainee/{username}/training")
    @Operation(summary = "add training")
    @Tag(name = "trainee")
    public ResponseEntity<Object> createTraineeTraining(@RequestBody CreateTrainingRequest request,
                                                        @PathVariable(name = "username") String username,
                                                        @RequestParam(name = "trainer") String trainerUsername){
        request.setTraineeUsername(username);
        request.setTrainerUsername(trainerUsername);
        if (trainingService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training creation rejected for unspecified reason");
    }
    @PostMapping("/trainer/{username}/training")
    @Operation(summary = "add training")
    @Tag(name = "trainer")
    public ResponseEntity<Object> createTrainerTraining(@RequestBody CreateTrainingRequest request,
                                                        @PathVariable(name = "username") String username,
                                                        @RequestParam(name = "trainee") String traineeUsername){
        request.setTrainerUsername(username);
        request.setTraineeUsername(traineeUsername);
        if (trainingService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training creation rejected for unspecified reason");
    }
}
