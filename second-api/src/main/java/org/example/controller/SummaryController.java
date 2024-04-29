package org.example.controller;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryResponse;
import org.example.security.JWTUtils;
import org.example.service.TrainingSummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin
public class SummaryController {
    @Autowired
    TrainingSummaryService service;

    @Autowired
    private JWTUtils jwtUtils;
    private final String accessUsername = "MAIN_SERVICE";
    private final String accessPassword = "SUPER_SECRET_PASSWORD";

    @PutMapping("/training")
    @Transactional
    void putTraining(@RequestBody SecondMicroservicePutTrainingRequest req){
        service.putTrainingRequest(req);
    }

    @PostMapping("/training_summary/{username}")
    TrainingDurationSummaryResponse getTrainingSummary(@RequestBody TrainingDurationSummaryRequest request,
                                                       @PathVariable(name = "username")  String username){
        return service.getTrainingSummary(request, username);
    }

    @PostMapping("/login/{username}")
    String login(@RequestBody String password,
                 @PathVariable(name = "username") String username) {
        if (!username.equals(accessUsername))
            return null;
        if (!password.equals(accessPassword))
            return null;
        UserDetails userDetails = User.withUsername(username)
                .authorities(AuthorityUtils.NO_AUTHORITIES)
                .password("HIDDEN")
                .build();
        return jwtUtils.generateToken(userDetails);
    }
}
