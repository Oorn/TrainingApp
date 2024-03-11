package org.example.security.bruteforce;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.stereotype.Component;


//This doesn't actually do anything because our authentication provider doesn't properly reject auth.
@Component
public class AuthFailListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    @Autowired
    BruteForceProtectionService targetService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        targetService.registerLoginFailure(event.getAuthentication().getName());
    }
}
