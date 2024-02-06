package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Setter;
import org.example.exceptions.NoSuchEntityException;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.example.requests_responses.trainer.CreateMentorRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.security.JWTPropertiesConfig;
import org.example.security.JWTUtils;
import org.example.service.CredentialsService;
import org.example.service.StudentService;
import org.example.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;

@RestController
public class CredentialsController {

    @Setter(onMethod_={@Autowired})
    private StudentService studentService;

    @Setter(onMethod_={@Autowired})
    private MentorService mentorService;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    @Setter(onMethod_={@Autowired})
    private JWTUtils jwtUtils;
    @PostMapping("/student")
    @Operation(summary = "register new student")
    @Tag(name = "student")
    @Transactional
    public ResponseEntity<Object> createStudent(@RequestBody CreateStudentRequest request){
        CredentialsResponse result = studentService.create(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    @PostMapping("/mentor")
    @Operation(summary = "register new mentor")
    @Tag(name = "mentor")
    @Transactional
    public ResponseEntity<Object> createMentor(@RequestBody CreateMentorRequest request){
        CredentialsResponse result = mentorService.create(request);
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }

    private ResponseEntity<Object> prepareTokenResponse(String username) {
        UserDetails userDetails = User.withUsername(username)
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .password("HIDDEN")
                .build();
        String token = jwtUtils.generateToken(userDetails);
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWTPropertiesConfig.AUTH_TOKEN_HEADER, token);
        return new ResponseEntity<>(token,
                headers,
                HttpStatus.OK);
    }

    @PostMapping("/student/{username}/login")
    @Operation(summary = "login")
    @Tag(name = "student")
    public ResponseEntity<Object> loginTrainee(@RequestBody String password,
                                               @PathVariable(name = "username") String username){
        if (!credentialsService.validateUsernamePassword(username, password))
            throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
        if (studentService.get(username) == null)
            throw new NoSuchEntityException("user is a mentor, not a student");

        return prepareTokenResponse(username);
    }

    @PostMapping("/mentor/{username}/login")
    @Operation(summary = "login")
    @Tag(name = "mentor")
    public ResponseEntity<Object> loginTrainer(@RequestBody String password,
                                               @PathVariable(name = "username") String username){
        if (!credentialsService.validateUsernamePassword(username, password))
            throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
        if (mentorService.get(username) == null)
            throw new NoSuchEntityException("user is a student, not a mentor");

        return prepareTokenResponse(username);

    }

    @PostMapping("/student/{username}/password")
    @Operation(summary = "change password", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Tag(name = "student")
    @Transactional
    public ResponseEntity<Object> updateStudentPassword(@RequestBody UpdateCredentialsRequest request,
                                                        @PathVariable(name = "username") String username){
        if (credentialsService.updateCredentials(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
    }

    @PostMapping("/mentor/{username}/password")
    @Operation(summary = "change password", parameters = {
            @Parameter(in = ParameterIn.HEADER
                    , description = "user auth token"
                    , name = JWTPropertiesConfig.AUTH_TOKEN_HEADER
                    , content = @Content(schema = @Schema(type = "string")))
    })
    @Tag(name = "mentor")
    @Transactional
    public ResponseEntity<Object> updateMentorPassword(@RequestBody UpdateCredentialsRequest request,
                                                       @PathVariable(name = "username") String username){
        if (credentialsService.updateCredentials(username, request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - credentials rejected for unspecified reason"); //service should have thrown another exception by now
    }
}
