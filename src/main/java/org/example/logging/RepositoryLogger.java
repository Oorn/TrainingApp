package org.example.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class RepositoryLogger {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Before("within(@org.springframework.stereotype.Repository *)")
    public void logRepositoryEvents(JoinPoint joinpoint) {
        String message = "repository event: calling method ";
        message += joinpoint.getSignature().getName();
        message += " of class ";
        message += joinpoint.getSignature().getDeclaringTypeName();
        message += " with arguments ";
        message += Arrays.stream(joinpoint.getArgs()).sequential()
                .map(o->o.getClass().getName() + ": " + o +", ").reduce("", (a,b)->a+b);
        message = message.substring(0, message.length() - 2);
        log.info(message);
    }
}
