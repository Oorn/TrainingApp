package org.example.service;

import org.example.domain_entities.User;

public interface CredentialsServiceUtils {
    String generateRandomPassword();
    String generateRandomSalt();
    String generatePasswordHash(String password);
    boolean validateUserPassword(User user, String password);

    boolean validatePasswordRequirements(String password);
}
