package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.service.TrainingService;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/training-type")
public class TrainingTypeController {
    @Setter(onMethod_={@Autowired})
    private TrainingTypeService trainingTypeService;

    @PostMapping
    @Operation(summary = "add training type")
    public ResponseEntity<Object> createTrainingType(@RequestParam String request){
        if (trainingTypeService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @GetMapping
    @Operation(summary = "get all training types")
    public ResponseEntity<Object> getAllTrainingTypes(){
        List<String> result = trainingTypeService.get();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
