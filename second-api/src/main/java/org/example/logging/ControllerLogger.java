package org.example.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.example.logging.request_backtrace.RequestBacktraceProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;



@Aspect
@Component
public class ControllerLogger {
    @Autowired
    private RequestBacktraceProvider backtraceProvider;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("within(@org.springframework.web.bind.annotation.RestController *) && execution(public * *(..))")
    public Object logServiceEvents(ProceedingJoinPoint joinPoint) throws Throwable {
        String message = "transaction: ";
        message += backtraceProvider.getCurrentBacktrace().getUuid();
        message += " controller event: ";
        ContentCachingRequestWrapper request = null;
        try {
            request = (ContentCachingRequestWrapper) backtraceProvider.getCurrentBacktrace().getRequest();
        }
        catch (Throwable ignored){}

        if (request == null)
            message += " WARNING - could not read request info; ";
        else {
            message += request.getMethod() + " " + request.getRequestURL() + "\n";
            String s = request.getPathInfo();
            if (s != null)
                message += "extra info: " + s;
            s = request.getQueryString();
            if (s != null)
                message += " query string: " + s + "\n";
            try {
                message += " request body: " + new String(request.getContentAsByteArray()).lines().reduce("", String::concat);
            }
            catch (Throwable t) {
                message += "WARNING - could not request body";
            }

        }

        message += "\n method: " + joinPoint.getSignature().getName();
        message += " of class ";
        message += joinPoint.getSignature().getDeclaringTypeName();

        try {
            Object result = joinPoint.proceed();
            try {
                ResponseEntity<Object> response = (ResponseEntity<Object>) result;
                message += "\n response code: " + response.getStatusCode();
                if (response.hasBody())
                    message += "\n response body: " + response.getBody();
            }
            catch (Throwable t) {
                message += "\nWARNING - could not convert return value to ResponseEntity<Object>. Using fallback toString";
                message += "\nmethod has completed with return value: " + result;
            }
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
