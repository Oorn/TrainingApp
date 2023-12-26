package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.TraineeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ping")
public class PingController {

    @Setter(onMethod_={@Autowired})
    private TraineeService traineeService;
    @GetMapping
    @Operation(summary = "gets latest messages from channel")
    public ResponseEntity<Object> ping(){
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

    @PostMapping("/test-create")
    @Operation(summary = "test create Trainee")
    public ResponseEntity<Object> testCreateTrainee(@RequestBody CreateTraineeRequest request){
        CredentialsResponse result = traineeService.create(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

