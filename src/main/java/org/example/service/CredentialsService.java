package org.example.service;

import org.example.requests_responses.user.UpdateCredentialsRequest;

public interface CredentialsService {
    boolean validateUsernamePassword(String username, String password);
    boolean updateCredentials (UpdateCredentialsRequest request);

    String generateUsername(String firstName, String lastName);

}
