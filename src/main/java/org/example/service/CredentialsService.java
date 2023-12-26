package org.example.service;

import org.example.requests_responses.user.LoginRequest;
import org.example.requests_responses.user.UpdateCredentialsRequest;

public interface CredentialsService {
    boolean validateUsernamePassword(String username, String password);

    boolean validateUsernamePassword(LoginRequest request);
    boolean updateCredentials (UpdateCredentialsRequest request);

    String generateUsername(String firstName, String lastName);

}
