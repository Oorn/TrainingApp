package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.Setter;
import org.example.requests_responses.training.CreateTrainingRequest;
import org.example.service.TrainingService;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;
import java.util.List;

@RestController
@RequestMapping("/training-types")
public class TrainingTypeController {
    @Setter(onMethod_={@Autowired})
    private TrainingTypeService trainingTypeService;

    @PostMapping
    @Operation(summary = "add training type")
    public ResponseEntity<Object> createTrainingType(@RequestParam String request){
        if (trainingTypeService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training type creation rejected for unspecified reason");
    }

    @GetMapping
    @Operation(summary = "get all training types")
    @Tags({@Tag(name = "trainee"), @Tag(name = "trainer")})
    public ResponseEntity<Object> getAllTrainingTypes(){
        List<String> result = trainingTypeService.get();
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }
}
