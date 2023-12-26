package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.Setter;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/trainee")
public class TraineeController {
    @Setter(onMethod_={@Autowired})
    private TraineeService traineeService;

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipService partnershipService;

    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @GetMapping
    @Operation(summary = "trainee info")
    public ResponseEntity<Object> getTrainee(@RequestParam String username){
        TraineeFullInfoResponse result = traineeService.get(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping
    @Operation(summary = "update trainee")
    public ResponseEntity<Object> updateTrainee(@RequestBody UpdateTraineeProfileRequest request){
        TraineeFullInfoResponse result = traineeService.update(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping
    @Operation(summary = "remove trainee")
    public ResponseEntity<Object> deleteTrainee(@RequestParam String username){
        if (traineeService.delete(username))
            return new ResponseEntity<>(HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/free-trainers")
    @Operation(summary = "available trainer info")
    public ResponseEntity<Object> getFreeTrainers(@RequestParam String username){
        AvailableTrainersResponse result = partnershipService.getNotAssignedTrainers(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/update-partnerships")
    @Operation(summary = "update training partnerships list")
    public ResponseEntity<Object> updatePartnerships(@RequestBody UpdateTrainingPartnershipListRequest request){
        UpdateTrainingPartnershipListResponse result = partnershipService.updateTraineeTrainerList(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/get-trainings")
    @Operation(summary = "associated trainings")
    public ResponseEntity<Object> getTrainings(@RequestBody GetTraineeTrainingsRequest request){
        MultipleTrainingInfoResponse result = trainingService.getByTrainee(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
