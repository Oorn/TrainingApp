package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.requests_responses.training.CreateTrainingForTraineeRequest;
import org.example.requests_responses.training.CreateTrainingForTrainerRequest;
import org.example.security.JWTPropertiesConfig;
import org.example.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;

@RestController
public class TrainingController {
    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @PostMapping("/trainee/{username}/training")
    @Operation(summary = "add training", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Tag(name = "trainee")
    @Transactional
    public ResponseEntity<Object> createTraineeTraining(@RequestBody CreateTrainingForTraineeRequest request,
                                                        @PathVariable(name = "username") String username){

        if (trainingService.create(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training creation rejected for unspecified reason");
    }
    @PostMapping("/trainer/{username}/training")
    @Operation(summary = "add training", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Tag(name = "trainer")
    @Transactional
    public ResponseEntity<Object> createTrainerTraining(@RequestBody CreateTrainingForTrainerRequest request,
                                                        @PathVariable(name = "username") String username){
        if (trainingService.create(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training creation rejected for unspecified reason");
    }
}
