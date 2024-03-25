package org.example.service.impl;

import org.example.domain_entities.User;
import org.example.exceptions.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class CredentialsServiceUtilsImplTest {

    CredentialsServiceUtilsImpl service = new CredentialsServiceUtilsImpl();

    @Test
    void generateRandomPassword() {
        String randPass1 = service.generateRandomPassword();
        String randPass2 = service.generateRandomPassword();

        assertNotEquals(randPass1,randPass2);
        assert CredentialsServiceUtilsImpl.RANDOM_PASSWORD_LENGTH == randPass1.length();
    }

    @Test
    void generateRandomSalt() {
        String randSalt1 = service.generateRandomSalt();
        String randSalt2 = service.generateRandomSalt();

        assertNotEquals(randSalt1,randSalt2);
        assert CredentialsServiceUtilsImpl.RANDOM_SALT_LENGTH == randSalt1.length();
    }

    @Test
    void generateStringHash() {
        String str1 = "str1", str2 = "str2";
        String hash1_1 = service.generateStringHash(str1), hash1_2 = service.generateStringHash(str1), hash2_1 = service.generateStringHash(str2);
        assertNotEquals(str1, hash1_1);
        assertNotEquals(hash1_1, hash1_2);
        assertNotEquals(hash1_1, hash2_1);

    }

    @Test
    void validateStringHash() {
        String str1 = "str1", str2 = "str2";
        String hash1_1 = service.generateStringHash(str1), hash1_2 = service.generateStringHash(str1), hash2_1 = service.generateStringHash(str2);

        assert service.validateStringHash(str1, hash1_1);
        assert service.validateStringHash(str1, hash1_2);
        assert !service.validateStringHash(str1, hash2_1);
    }

    @Test
    void validateUserPassword() {
        String password1 = "password1", password2 = "password2", salt1 = "salt1", salt2 = "salt2";
        User user = User.builder().build();
        service.setUserPassword(user, password1);

        //testing for correct password being accepted and incorrect being rejected
        assert service.validateUserPassword(user, password1);
        assert !service.validateUserPassword(user, password2);
        //testing for changing salt influencing password validation
        user.setPasswordSalt(salt2);
        assert !service.validateUserPassword(user, password1);

    }

    @Test
    void setUserPassword() {
        String password1 = "password1", password2 = "password2";
        User user = User.builder().build();

        //testing that setting password always changes both password and salt fields
        service.setUserPassword(user, password1);
        String passwordField1_1 = user.getPassword(), saltField1_1 = user.getPasswordSalt();
        service.setUserPassword(user, password2);
        String passwordField2_1 = user.getPassword(), saltField2_1 = user.getPasswordSalt();
        service.setUserPassword(user, password1);
        String passwordField1_2 = user.getPassword(), saltField1_2 = user.getPasswordSalt();

        assertNotEquals(passwordField1_1, passwordField2_1);
        assertNotEquals(saltField1_1, saltField2_1);
        assertNotEquals(passwordField1_1, passwordField1_2);
        assertNotEquals(saltField1_1, saltField1_2);
    }


    static Stream<Arguments> validatePasswordRequirementsTestArguments() {
        return Stream.of(Arguments.of("valid", true),
                Arguments.of("shor", false),
                Arguments.of("symbol/", true),
                Arguments.of("spa ces", false));
    }
    @ParameterizedTest
    @MethodSource("validatePasswordRequirementsTestArguments")
    void validatePasswordRequirements(String password, boolean expectedResult) {
        try {
            service.validatePasswordRequirements(password);
            assert expectedResult;
        }
        catch (BadRequestException ex) {
            assert  !expectedResult;
        }
    }
}