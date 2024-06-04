package org.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.logging.request_backtrace.RequestBacktraceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class ServiceLogger {

    @Autowired
    private RequestBacktraceProvider backtraceProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("within(@org.springframework.stereotype.Repository *) && execution(public * *(..))")
    public Object logServiceEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        String message = "transaction: ";
        message += backtraceProvider.getCurrentBacktrace().getUuid();
        message += " service event: calling method ";
        message += joinPoint.getSignature().getName();
        message += " of class ";
        message += joinPoint.getSignature().getDeclaringTypeName();
        message += " with arguments:  ";
        message += Arrays.stream(joinPoint.getArgs()).sequential()
                .map(o->o.getClass().getName() + " " + o +", ").reduce("", (a,b)->a+b);
        message = message.substring(0, message.length() - 2);


        try {
            Object result = joinPoint.proceed();
            message += "\n method has completed with return value: ";
            message += result;
            log.info(message);
            return result;

        } catch (Throwable e) {
            message += "\n method execution produced exception: ";
            message += e.getMessage();
            log.info(message);
            throw e;
        }
    }
}
