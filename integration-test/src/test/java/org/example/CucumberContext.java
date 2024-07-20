package org.example;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

@CucumberContextConfiguration
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {Main.class, CucumberContext.class})
@ContextConfiguration
public class CucumberContext {

    static ContainerConfigIntegrationTest containers = null;
    static boolean containersAreRunning = false;

    @Bean
    static ContainerConfigIntegrationTest containers() throws InterruptedException {
        if (containers == null)
            containers = new ContainerConfigIntegrationTest().prepareContainers();
        if (!containersAreRunning)
            containers.startContainers();
        return containers;
    }
}
