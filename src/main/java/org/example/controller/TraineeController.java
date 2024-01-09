package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.exceptions.NoSuchEntityException;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainee.UpdateTraineeProfileRequest;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.TraineeService;
import org.example.service.TrainerService;
import org.example.service.TrainingPartnershipService;
import org.example.service.TrainingService;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

@RestController
@RequestMapping("/trainee/{username}")
@Tag(name = "trainee")
public class TraineeController {
    @Setter(onMethod_={@Autowired})
    private TraineeService traineeService;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipService partnershipService;

    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @GetMapping
    @Operation(summary = "trainee info")
    public ResponseEntity<Object> getTrainee(@PathVariable(name = "username") String username){
        TraineeFullInfoResponse result = traineeService.get(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PutMapping
    @Operation(summary = "update trainee")
    public ResponseEntity<Object> updateTrainee(@RequestBody UpdateTraineeProfileRequest request,
                                                @PathVariable(name = "username") String username){
        request.setUsername(username);
        TraineeFullInfoResponse result = traineeService.update(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @DeleteMapping
    @Operation(summary = "remove trainee")
    public ResponseEntity<Object> deleteTrainee(@PathVariable(name = "username") String username){
        if (traineeService.delete(username))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - delete rejected for unspecified reason");
    }

    @GetMapping("/free-trainers")
    @Operation(summary = "available trainer info")
    public ResponseEntity<Object> getFreeTrainers(@PathVariable(name = "username") String username){
        AvailableTrainersResponse result = partnershipService.getNotAssignedTrainers(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PutMapping("/partnerships")
    @Operation(summary = "update trainees training partnerships list")
    public ResponseEntity<Object> updatePartnerships(@ModelAttribute @RequestBody UpdateTrainingPartnershipListRequest request,
                                                     @PathVariable(name = "username") String username){
        request.setUsername(username);
        UpdateTrainingPartnershipListResponse result = partnershipService.updateTraineeTrainerList(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @GetMapping("/trainings")
    @Operation(summary = "associated trainings")
    public ResponseEntity<Object> getTrainings(@ParameterObject GetTraineeTrainingsRequest request,
                                               @PathVariable(name = "username") String username){
        request.setUsername(username);
        MultipleTrainingInfoResponse result = trainingService.getByTrainee(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }
}
