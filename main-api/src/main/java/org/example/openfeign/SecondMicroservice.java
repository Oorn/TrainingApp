package org.example.openfeign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "second-api", fallback = SecondMicroserviceFallback.class)
public interface SecondMicroservice {

    @GetMapping("/ping")
    String ping();
}
