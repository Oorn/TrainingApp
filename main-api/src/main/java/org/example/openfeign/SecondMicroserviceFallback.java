package org.example.openfeign;

import org.springframework.stereotype.Component;

@Component
public class SecondMicroserviceFallback implements SecondMicroservice{
    @Override
    public String ping() {
        return "second service not pong";
    }
}
