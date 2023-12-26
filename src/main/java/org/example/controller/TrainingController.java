package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/training")
public class TrainingController {
    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @PostMapping
    @Operation(summary = "add training")
    public ResponseEntity<Object> createTraining(@RequestBody CreateTrainingRequest request){
        if (trainingService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
