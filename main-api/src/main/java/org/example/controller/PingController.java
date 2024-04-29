package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.example.to_externalize.jms.SecondMicroserviceJmsService;
import org.example.to_externalize.SecondMicroserviceWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ping")
@CrossOrigin
public class PingController {

    @Autowired
    SecondMicroserviceWrapper secondMicroservice;
    @Autowired
    SecondMicroserviceJmsService secondMicroserviceJmsService;

    @GetMapping
    @Operation(summary = "simple ping")
    public ResponseEntity<Object> ping(){
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

    @GetMapping("/micro")
    @Operation(summary = "microservice ping")
    public ResponseEntity<Object> pingMicro(){
        return new ResponseEntity<>(secondMicroservice.ping(), HttpStatus.OK);
    }

    @GetMapping("/jms_micro")
    @Operation(summary = "jms messaging ping")
    public ResponseEntity<Object> pingJms(){
        secondMicroserviceJmsService.ping();
        return new ResponseEntity<>("pinged with unknown results, check logs", HttpStatus.OK);
    }

}

