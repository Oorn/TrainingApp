package org.example.service;

import org.example.domain_entities.User;

public interface CredentialsServiceUtils {
    String generateUsername(String firstName, String lastName);
    String generateRandomPassword();
    String generateRandomSalt();
    String generatePasswordHash();
    boolean validateUserPassword(User user, String password);
}
