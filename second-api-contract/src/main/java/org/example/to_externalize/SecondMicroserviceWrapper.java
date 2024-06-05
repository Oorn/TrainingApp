package org.example.to_externalize;

import com.auth0.jwt.JWT;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

//controls access to second microservice.
//handles automatic login, and re-login after service crash or token expiration
@Service
public class SecondMicroserviceWrapper{
    @Autowired
    SecondMicroservice service;

    @Autowired(required = false)
    @Qualifier("logging-uuid-export")
    private Supplier<String> uuidSupplier;

    private String token = null;

    private final Duration grace = Duration.ofMinutes(1);
    private final String accessUsername = "MAIN_SERVICE";
    private final String accessPassword = "SUPER_SECRET_PASSWORD";

    @PostConstruct
    private void defaultUuid() {
        if (uuidSupplier == null)
            uuidSupplier = () -> "no-id-supplier-presented";
    }

    public void updateToken() {
        if (token == null)
            token = service.login(uuidSupplier.get(), accessPassword, accessUsername);
        try {
            if (JWT.decode(token).getExpiresAt().toInstant().minus(grace).isBefore(Instant.now()))
                token = service.login(uuidSupplier.get(), accessPassword, accessUsername);
        }
        catch (Exception ex) {
            token = service.login(uuidSupplier.get(), accessPassword, accessUsername);
        }
    }
    public void resetToken() {
        token = null;
    }

    public String ping() {
        updateToken();
        return service.ping(token, uuidSupplier.get());
    }

    public void putTraining(SecondMicroservicePutTrainingRequest req) {
        updateToken();
        service.putTraining(token, uuidSupplier.get(), req);

    }

    public TrainingDurationSummaryResponse getTrainingSummary(TrainingDurationSummaryRequest request, String username) {
        updateToken();
        return service.getTrainingSummary(token, uuidSupplier.get(), request, username);
    }

    public Long getTrainingSummaryCount() {
        updateToken();
        return service.getTrainingSummaryCount(token, uuidSupplier.get());

    }

    public String login(String password, String username) {
        token = service.login(uuidSupplier.get(), password, username);
        return token;
    }
}
