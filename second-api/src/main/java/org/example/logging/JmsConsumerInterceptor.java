package org.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.logging.request_backtrace.RequestBacktraceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.Message;
import java.util.Arrays;

@Aspect
@Component
public class JmsConsumerInterceptor {
    @Autowired
    private RequestBacktraceProvider backtraceProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    //@Around(value = "execution (* javax.jms.MessageListener.onMessage(..))")
    @Around("@annotation(org.springframework.jms.annotation.JmsListener)")
    public Object logServiceEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        Message msg = (Message) Arrays.stream(joinPoint.getArgs()).filter(m -> m instanceof Message).findAny().orElse(null);
        if (msg == null) {
            log.warn("JMS message listener triggered pointcut without Message param - add Message param to your listener for logging");
            String message = " listener method ";
            message += joinPoint.getSignature().getName();
            message += " of class ";
            message += joinPoint.getSignature().getDeclaringTypeName();
            message += " with arguments:  ";
            message += Arrays.stream(joinPoint.getArgs()).sequential()
                    .map(o->o.getClass().getName() + " " + o +", ").reduce("", (a,b)->a+b);
            message = message.substring(0, message.length() - 2);
            log.info(message);
            return joinPoint.proceed();
        }
        backtraceProvider.recordCurrentJMSMessage(msg);

        String message = "transaction: ";
        message += backtraceProvider.getCurrentBacktrace().getUuid();
        message += " listener method ";
        message += joinPoint.getSignature().getName();
        message += " of class ";
        message += joinPoint.getSignature().getDeclaringTypeName();
        message += " JMS message received: ";
        message += msg;


        try {
            Object result = joinPoint.proceed();
            backtraceProvider.cleanCurrentBacktrace();
            log.info(message);
            return result;

        } catch (Throwable e) {
            message += " message handling produced exception: ";
            message += e.getMessage();
            backtraceProvider.cleanCurrentBacktrace();
            log.info(message);
            throw e;
        }

    }

}
