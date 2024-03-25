package org.example.security.bruteforce;

import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class BruteForceProtectionService {

    private final Duration RESTRICT_DURATION = Duration.ofMinutes(5);

    private final Duration FORGET_FAILS_AFTER = Duration.ofMinutes(5);

    private final int NUMBER_OF_FAILED_ATTEMPTS_BEFORE_RESTRICT = 3;

    private final Map<String, Integer> failedAttemptsNumber = new HashMap<>();

    private final Map<String, Instant> lastFailedAttempts = new HashMap<>();

    public void registerLoginFailure(String username) {
        Instant lastFail = lastFailedAttempts.get(username);
        if (lastFail == null) {
            //first time this user failed
            lastFailedAttempts.put(username, Instant.now());
            failedAttemptsNumber.put(username, 1);
        }
        else if (Instant.now().isAfter(lastFail.plus(FORGET_FAILS_AFTER))) {
            //last fail was long ago, reset counter
            lastFailedAttempts.put(username, Instant.now());
            failedAttemptsNumber.put(username, 1);
        }
        else {
            //increment failed number
            lastFailedAttempts.put(username, Instant.now());
            failedAttemptsNumber.put(username, failedAttemptsNumber.get(username) + 1);
        }

    }
    public boolean verifyLoginAccess(String username) {
        Instant lastFail = lastFailedAttempts.get(username);
        if (lastFail == null)
            return true;
        return (failedAttemptsNumber.get(username) < NUMBER_OF_FAILED_ATTEMPTS_BEFORE_RESTRICT)
                || (lastFail.plus(RESTRICT_DURATION).isBefore(Instant.now()));
    }
}
