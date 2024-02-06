package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/ping")
public class PingController {
    @GetMapping
    @Operation(summary = "simple ping")
    public ResponseEntity<Object> ping(){
        return new ResponseEntity<>("pong", HttpStatus.OK);
    }

}

