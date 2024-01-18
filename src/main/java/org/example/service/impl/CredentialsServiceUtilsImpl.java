package org.example.service.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.example.domain_entities.User;
import org.example.exceptions.BadRequestException;
import org.example.service.CredentialsServiceUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class CredentialsServiceUtilsImpl implements CredentialsServiceUtils {

    public static final int MIN_PASSWORD_LENGTH = 5;

    public static final int MAX_PASSWORD_LENGTH = 40;
    public static final int RANDOM_PASSWORD_LENGTH = 10;
    public static final int RANDOM_SALT_LENGTH = 10;
    private static final int POSSIBLE_CHARACTER_START = 33; // ! symbol
    private static final int POSSIBLE_CHARACTER_END = 127; // ~ symbol

    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();
    @Override
    public String generateRandomPassword() {
        return RandomStringUtils.random(RANDOM_PASSWORD_LENGTH, POSSIBLE_CHARACTER_START, POSSIBLE_CHARACTER_END, true, true, null, new SecureRandom());
    }

    @Override
    public String generateRandomSalt() {
        return RandomStringUtils.random(RANDOM_SALT_LENGTH, POSSIBLE_CHARACTER_START, POSSIBLE_CHARACTER_END, true, true, null, new SecureRandom());
    }

    @Override
    public String generateStringHash(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean validateStringHash(String submittedString, String hashedString) {
        return encoder.matches(submittedString,hashedString);
    }

    @Override
    public boolean validateUserPassword(User user, String password) {
        return validateStringHash(password + user.getPasswordSalt(), user.getPassword());
    }

    @Override
    public void setUserPassword(User user, String password) {
        String newSalt = generateRandomSalt();
        String newHashedPassword = generateStringHash(password + newSalt);
        user.setPasswordSalt(newSalt);
        user.setPassword(newHashedPassword);
    }

    @Override
    public boolean validatePasswordRequirements(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH)
            throw new BadRequestException("password too short");
        if (password.length() > MAX_PASSWORD_LENGTH)
            throw new BadRequestException("password too long");

        password.chars().forEach(c -> {
            if ((c < POSSIBLE_CHARACTER_START) || (c > POSSIBLE_CHARACTER_END))
                throw new BadRequestException("password has illegal characters");
        });
        return true;
    }
}
