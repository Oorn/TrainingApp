package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.exceptions.NoSuchEntityException;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.service.CredentialsService;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;

@RestController
public class CredentialsController {

    @Setter(onMethod_={@Autowired})
    private TraineeService traineeService;

    @Setter(onMethod_={@Autowired})
    private TrainerService trainerService;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;
    @PostMapping("/trainee")
    @Operation(summary = "register new Trainee")
    @Tag(name = "trainee")
    @Transactional
    public ResponseEntity<Object> createTrainee(@RequestBody CreateTraineeRequest request){
        CredentialsResponse result = traineeService.create(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PostMapping("/trainer")
    @Operation(summary = "register new Trainer")
    @Tag(name = "trainer")
    @Transactional
    public ResponseEntity<Object> createTrainer(@RequestBody CreateTrainerRequest request){
        CredentialsResponse result = trainerService.create(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PostMapping("/trainee/{username}/login")
    @Operation(summary = "login")
    @Tag(name = "trainee")
    public ResponseEntity<Object> loginTrainee(@RequestBody String password,
                                               @PathVariable(name = "username") String username){
        if (!credentialsService.validateUsernamePassword(username, password))
            throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
        if (traineeService.get(username) == null)
            throw new NoSuchEntityException("user is a trainer, not a trainee");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/trainer/{username}/login")
    @Operation(summary = "login")
    @Tag(name = "trainer")
    public ResponseEntity<Object> loginTrainer(@RequestBody String password,
                                               @PathVariable(name = "username") String username){
        if (!credentialsService.validateUsernamePassword(username, password))
            throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
        if (trainerService.get(username) == null)
            throw new NoSuchEntityException("user is a trainee, not a trainer");
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping("/trainee/{username}/password")
    @Operation(summary = "change password")
    @Tag(name = "trainee")
    @Transactional
    public ResponseEntity<Object> updateTraineePassword(@RequestBody UpdateCredentialsRequest request,
                                        @PathVariable(name = "username") String username){
        if (credentialsService.updateCredentials(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
    }

    @PostMapping("/trainer/{username}/password")
    @Operation(summary = "change password")
    @Tag(name = "trainer")
    @Transactional
    public ResponseEntity<Object> updateTrainerPassword(@RequestBody UpdateCredentialsRequest request,
                                        @PathVariable(name = "username") String username){
        if (credentialsService.updateCredentials(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
    }
}
