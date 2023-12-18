package org.example.service;

import org.example.domain_entities.User;

public interface CredentialsServiceUtils {
    String generateRandomPassword();
    String generateRandomSalt();
    String generateStringHash(String password);

    boolean validateStringHash(String submittedString, String hashedString);
    boolean validateUserPassword(User user, String password);

    void setUserPassword(User user, String password);

    boolean validatePasswordRequirements(String password);
}
