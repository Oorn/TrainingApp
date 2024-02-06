package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.trainer.UpdateMentorProfileRequest;
import org.example.requests_responses.training.GetMentorTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.security.JWTPropertiesConfig;
import org.example.service.MentorService;
import org.example.service.TrainingService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/mentor/{username}")
@Tag(name = "mentor")
public class MentorController {
    @Setter(onMethod_={@Autowired})
    private MentorService mentorService;
    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @GetMapping
    @Operation(summary = "mentor info", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    public ResponseEntity<Object> getMentor(@PathVariable(name = "username") String username){
        MentorFullInfoResponse result = mentorService.get(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PutMapping
    @Operation(summary = "update mentor", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Transactional
    public ResponseEntity<Object> updateMentor(@RequestBody UpdateMentorProfileRequest request,
                                               @PathVariable(name = "username") String username) {
        MentorFullInfoResponse result = mentorService.update(username, request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @GetMapping("/trainings")
    @Operation(summary = "associated trainings", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    public ResponseEntity<Object> getTrainings(@ParameterObject GetMentorTrainingsRequest request,
                                               @PathVariable(name = "username") String username){
        MultipleTrainingInfoResponse result = trainingService.getByMentor(username, request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }
}
