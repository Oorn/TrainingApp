package org.example.to_externalize;

import com.auth0.jwt.JWT;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.Duration;
import java.time.Instant;

//controls access to second microservice.
//handles automatic login, and re-login after service crash or token expiration
@Service
public class SecondMicroserviceWrapper{
    @Autowired
    SecondMicroservice service;

    private String token = null;

    private final Duration grace = Duration.ofMinutes(1);
    private final String accessUsername = "MAIN_SERVICE";
    private final String accessPassword = "SUPER_SECRET_PASSWORD";
    public void updateToken() {
        if (token == null)
            token = service.login(accessPassword, accessUsername);
        try {
            if (JWT.decode(token).getExpiresAt().toInstant().minus(grace).isBefore(Instant.now()))
                token = service.login(accessPassword, accessUsername);
        }
        catch (Exception ex) {
            token = service.login(accessPassword, accessUsername);
        }
    }
    public void resetToken() {
        token = null;
    }

    public String ping() {
        updateToken();
        return service.ping(token);
    }

    public void putTraining(SecondMicroservicePutTrainingRequest req) {
        updateToken();
        service.putTraining(token, req);

    }

    public TrainingDurationSummaryResponse getTrainingSummary(TrainingDurationSummaryRequest request, String username) {
        updateToken();
        return service.getTrainingSummary(token, request, username);
    }

    public Long getTrainingSummaryCount() {
        updateToken();
        return service.getTrainingSummaryCount(token);

    }

    public String login(String password, String username) {
        token = service.login(password, username);
        return token;
    }
}
