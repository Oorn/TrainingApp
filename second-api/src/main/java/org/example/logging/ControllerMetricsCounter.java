package org.example.logging;

import io.micrometer.core.instrument.Metrics;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.Duration;


@Aspect
@Component
public class ControllerMetricsCounter {

    @Around("within(@org.springframework.web.bind.annotation.RestController *) && execution(public * *(..))")
    public Object logServiceEvents(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();

        //already recorded as part of timer anyway
        //Metrics.counter("controller.times.called", "method", methodName).increment();

        long nanoStart = System.nanoTime();
        try {
            return joinPoint.proceed();
        }
        finally {
            Duration duration = Duration.ofNanos(System.nanoTime() - nanoStart);
            Metrics.timer("controller.call.duration", "method", methodName).record(duration);
        }
    }
}
