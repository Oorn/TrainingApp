package org.example.service.impl;


import org.example.domain_entities.User;
import org.example.exceptions.NoPermissionException;
import org.example.repository.UserHibernateRepository;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.service.CredentialsServiceUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CredentialsServiceImplTest {

    @Mock
    private CredentialsServiceUtils credentialsServiceUtils;
    @Mock
    private UserHibernateRepository userRepository;
    @InjectMocks
    CredentialsServiceImpl credentialsService;

    @Test
    void validateUsernamePassword() {
        String username = "username", passwordRight = "passwordRight", passwordWrong = "passwordWrong";
        User user = User.builder().build();

        when(userRepository.findUserByUserName(username)).thenReturn(Optional.of(user));
        when(credentialsServiceUtils.validateUserPassword(user,passwordRight)).thenReturn(true);
        when(credentialsServiceUtils.validateUserPassword(user,passwordWrong)).thenReturn(false);

        assert credentialsService.validateUsernamePassword(username, passwordRight);
        assertThrows(NoPermissionException.class, () -> credentialsService.validateUsernamePassword(username, passwordWrong));
        verify(credentialsServiceUtils, times(2)).validateUserPassword(eq(user), anyString());

    }

    @Test
    void updateCredentials() {
        String username = "username", passwordOld = "passwordOld", passwordNew = "passwordNew";
        UpdateCredentialsRequest request = UpdateCredentialsRequest.builder()
                .oldPassword(passwordOld)
                .newPassword(passwordNew)
                .build();
        User user = User.builder().build();

        when(userRepository.findUserByUserName(username)).thenReturn(Optional.of(user));
        when(credentialsServiceUtils.validateUserPassword(user,passwordOld)).thenReturn(true);
        when(credentialsServiceUtils.validatePasswordRequirements(passwordNew)).thenReturn(true);

        assert credentialsService.updateCredentials(username, request);
        verify(credentialsServiceUtils, times(1)).setUserPassword(user, passwordNew);
    }

    @Test
    void generateUsername() {
        String firstName = "first", lastName = "last", expectedUsername = "first.last3";
        List<String> existingPrefixUsernames = List.of("first.last", "first.last0", "first.last1", "first.last2");
        String candidateUsername = firstName + "." + lastName;

        List<User> existingPrefixUsers = existingPrefixUsernames.stream()
                .map(s-> User.builder()
                        .userName(s)
                        .build())
                .collect(Collectors.toList());
        when(userRepository.findUsersByUserNameIsStartingWith(candidateUsername)).thenReturn(existingPrefixUsers);

        assertEquals(expectedUsername, credentialsService.generateUsername(firstName,lastName));
        verify(userRepository, times(1)).findUsersByUserNameIsStartingWith(candidateUsername);
    }
}