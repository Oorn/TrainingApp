package org.example;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Main.class, IntegrationContext.class})
@ContextConfiguration
public class IntegrationContext {

    static TestContainersEnvironment containers = null;


    @Bean
    static TestContainersEnvironment containers() throws InterruptedException {
        if (containers == null)
            containers = new TestContainersEnvironment().prepareContainers();
        if (!containers.containersAreRunning)
            containers.startContainers();
        return containers;
    }
}
