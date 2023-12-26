package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.requests_responses.user.LoginRequest;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.service.CredentialsService;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/credentials")
public class CredentialsController {

    @Setter(onMethod_={@Autowired})
    private TraineeService traineeService;

    @Setter(onMethod_={@Autowired})
    private TrainerService trainerService;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;
    @PostMapping("/register-trainee")
    @Operation(summary = "register Trainee")
    public ResponseEntity<Object> createTrainee(@RequestBody CreateTraineeRequest request){
        CredentialsResponse result = traineeService.create(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/register-trainer")
    @Operation(summary = "register Trainer")
    public ResponseEntity<Object> createTrainer(@RequestBody CreateTrainerRequest request){
        CredentialsResponse result = trainerService.create(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/login")
    @Operation(summary = "login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest request){
        if (credentialsService.validateUsernamePassword(request))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/update-credentials")
    @Operation(summary = "change credentials")
    public ResponseEntity<Object> login(@RequestBody UpdateCredentialsRequest request){
        if (credentialsService.updateCredentials(request))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN); //todo should forbidden on non-matching credentials, and bad request on not following password rules
    }
}
