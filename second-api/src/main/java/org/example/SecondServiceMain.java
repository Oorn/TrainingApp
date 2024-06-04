package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication(scanBasePackages = "org.example")
@EnableDiscoveryClient
public class SecondServiceMain {
    public static void main(String[] args) {
        SpringApplication.run(SecondServiceMain.class, args);
    }


}