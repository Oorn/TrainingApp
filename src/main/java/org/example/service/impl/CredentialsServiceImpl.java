package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.User;
import org.example.repository.UserRepository;
import org.example.requests_responses.user.UpdateCredentialsRequest;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CredentialsServiceImpl implements CredentialsService {

    @Setter(onMethod_={@Autowired})
    private CredentialsServiceUtils credentialsServiceUtils;

    @Setter(onMethod_={@Autowired})
    private UserRepository userRepository;

    @Override
    public boolean validateUsernamePassword(String username, String password) {
        Optional<User> optionalUser = userRepository.get(username);
        if (optionalUser.isEmpty())
            return false; //no user with such username found
        return credentialsServiceUtils.validateUserPassword(optionalUser.get(),password);
    }

    @Override
    public boolean updateCredentials(UpdateCredentialsRequest request) {
        if (!validateUsernamePassword(request.getUsername(), request.getOldPassword()))
            return false; //old credentials don't match
        if (!credentialsServiceUtils.validatePasswordRequirements(request.getNewPassword()))
            return false; //password does not meet requirements //todo add exceptions to indicate exact problem
        //checks passed, may update password

        User user = userRepository.get(request.getUsername()).orElseThrow();
        credentialsServiceUtils.setUserPassword(user, request.getNewPassword());
        userRepository.save(user);
        return true;
    }

    @Override
    public String generateUsername(String firstName, String lastName) {
        String defaultUsername = firstName + "." + lastName;
        String defaultUsernameRegex = Pattern.quote(defaultUsername); //to avoid sneaky injections
        List<User> matchingUsers = userRepository.getAllByPrefix(defaultUsername);
        if (matchingUsers.isEmpty())
            return defaultUsername; //no users with matching prefix, can use default
        List<String> matchingUsernamePostfixes = matchingUsers
                .stream()
                .map(User::getUserName)//convert to username stream
                .map(s -> s.replaceFirst("^" + defaultUsernameRegex, ""))//cut off defaultUsername, but only in the beginning of the String
                .collect(Collectors.toList());
        if (!matchingUsernamePostfixes.contains(""))
            return defaultUsername; //defaultUsername is present as a prefix, but not as actual username

        //more stream processing to isolate only positive number postfixes, to avoid manual checking for every int, which would be quadratic
        //this version is nlog(n)
        List<Long> matchingUsernameLongPostfixes = matchingUsernamePostfixes
                .stream()
                .filter(s-> s.matches("\\d+")) //remove everything that isn't positive long
                .map(s -> {
                    try { return Long.parseLong(s); } //convert to longs
                    catch (NumberFormatException e) { return -1L;} //edge case of number string possibly not parsing due to length
                })
                .filter(l->l!=-1L) //remove all instances that didn't parse correctly
                .sorted()
                .collect(Collectors.toList());
        long candidatePostfix = 0L;
        for (long presentPostfix:matchingUsernameLongPostfixes ) {
            if (candidatePostfix < presentPostfix)
                break; //candidate postfix has been passed without being equal, it is valid
            if (candidatePostfix > presentPostfix)
                continue; //candidate is larger than the present, move to next present. //todo log error, this state should be impossible without duplicate usernames
            candidatePostfix++; //candidate is equal to present, move to next candidate
        }

        //n^2 version which is much simpler
        /*long candidatePostfix = 0L;
        while (matchingUsernamePostfixes.contains(Long.toString(candidatePostfix))){
            candidatePostfix++;
        }*/

        return defaultUsername + candidatePostfix;
    }
}
