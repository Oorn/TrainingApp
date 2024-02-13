package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainee.UpdateStudentProfileRequest;
import org.example.requests_responses.training.GetStudentTrainingsRequest;
import org.example.requests_responses.training.MultipleTrainingInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.example.security.JWTPropertiesConfig;
import org.example.service.StudentService;
import org.example.service.PartnershipService;
import org.example.service.TrainingService;
import org.example.validation.ValidationConstants;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.Size;

@RestController
@RequestMapping("/student/{username}")
@Tag(name = "student")
public class StudentController {
    @Setter(onMethod_={@Autowired})
    private StudentService studentService;

    @Setter(onMethod_={@Autowired})
    private PartnershipService partnershipService;

    @Setter(onMethod_={@Autowired})
    private TrainingService trainingService;

    @GetMapping
    @Operation(summary = "student info", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    public ResponseEntity<Object> getStudent(@PathVariable(name = "username")
                                             @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                             String username){
        StudentFullInfoResponse result = studentService.get(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PutMapping
    @Operation(summary = "update student", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Transactional
    public ResponseEntity<Object> updateStudent(@RequestBody @Valid UpdateStudentProfileRequest request,
                                                @PathVariable(name = "username")
                                                @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                                String username){
        StudentFullInfoResponse result = studentService.update(username, request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @DeleteMapping
    @Operation(summary = "remove student", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Transactional
    public ResponseEntity<Object> deleteStudent(@PathVariable(name = "username")
                                                @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                                String username){
        if (studentService.delete(username))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - delete rejected for unspecified reason");
    }

    @GetMapping("/free-mentors")
    @Operation(summary = "available mentor info", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    public ResponseEntity<Object> getFreeMentors(@PathVariable(name = "username")
                                                 @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                                 String username){
        AvailableTrainersResponse result = partnershipService.getNotAssignedMentors(username);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PutMapping("/partnerships")
    @Operation(summary = "update students partnerships list", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Transactional
    public ResponseEntity<Object> updatePartnerships(@RequestBody @Valid UpdateTrainingPartnershipListRequest request,
                                                     @PathVariable(name = "username")
                                                     @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                                     String username){
        UpdateTrainingPartnershipListResponse result = partnershipService.updateStudentMentorsList(username, request);
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
    public ResponseEntity<Object> getTrainings(@ParameterObject @Valid GetStudentTrainingsRequest request,
                                               @PathVariable(name = "username")
                                               @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
                                               String username){
        MultipleTrainingInfoResponse result = trainingService.getByStudent(username, request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }
}
