package org.example;

//import io.cucumber.java.AfterAll;
//import io.cucumber.java.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
//@Testcontainers
//@ExtendWith(SpringExtension.class)
//@RunWith(Cucumber.class)
//@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Main.class)
//@CucumberContextConfiguration
//@ContextConfiguration
//@CucumberOptions(plugin = {"pretty"}, features = "src/test/resources/gherkin", glue = "org.example")
//@Component
public class ContainerConfigIntegrationTest {

    private final static File SECOND_SERVICE_LOCATION = new File("../second-api/target");
    private final static File MAIN_SERVICE_LOCATION = new File("../main-api/target");

    private final static File EUREKA_SERVICE_LOCATION = new File("../eureka/target");
    private final static String SECOND_SERVICE_NAME = "TrainingApp-second.jar";
    private final static String MAIN_SERVICE_NAME = "TrainingApp-main.jar";

    private final static String EUREKA_SERVICE_NAME = "TrainingApp-eureka.jar";



     /*static public Network network;

     static public GenericContainer eurekaServer;

     static public GenericContainer activeMQ;

     static public GenericContainer postgreSQLContainer;


    static public GenericContainer mainApi;


    static public GenericContainer secondApi;

    static public boolean containerAreRunning = false;

    static {
        network =  Network.newNetwork();
        postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
                .withDatabaseName("training_app")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432).withNetwork(network).withNetworkAliases("database");
        activeMQ = new GenericContainer("apache/activemq-classic:5.18.3")
                .withExposedPorts(61616).withNetwork(network).withNetworkAliases("activemq");
        eurekaServer = prepareTestContainer(EUREKA_SERVICE_LOCATION,EUREKA_SERVICE_NAME,"eureka",8761,network);
        mainApi = prepareTestContainer(MAIN_SERVICE_LOCATION,MAIN_SERVICE_NAME, "main-api", 8080,network)
                .dependsOn(postgreSQLContainer,activeMQ,eurekaServer);
        secondApi = prepareTestContainer(SECOND_SERVICE_LOCATION,SECOND_SERVICE_NAME, "second-api", 8080, network)
                .dependsOn(postgreSQLContainer,activeMQ,eurekaServer);

    }*/public Network network;

    public GenericContainer eurekaServer;

    public GenericContainer activeMQ;

    public GenericContainer postgreSQLContainer;


    public GenericContainer mainApi;


    public GenericContainer secondApi;

    //public boolean containerAreRunning = false;

    public ContainerConfigIntegrationTest prepareContainers () {
        network =  Network.newNetwork();
        postgreSQLContainer = new PostgreSQLContainer("postgres:11.1")
                .withDatabaseName("training_app")
                .withUsername("postgres")
                .withPassword("postgres")
                .withExposedPorts(5432).withNetwork(network).withNetworkAliases("database");
        activeMQ = new GenericContainer("apache/activemq-classic:5.18.3")
                .withExposedPorts(61616).withNetwork(network).withNetworkAliases("activemq");
        eurekaServer = prepareTestContainer(EUREKA_SERVICE_LOCATION,EUREKA_SERVICE_NAME,"eureka",8761,network);
        mainApi = prepareTestContainer(MAIN_SERVICE_LOCATION,MAIN_SERVICE_NAME, "main-api", 8080,network)
                .dependsOn(postgreSQLContainer,activeMQ,eurekaServer);
        secondApi = prepareTestContainer(SECOND_SERVICE_LOCATION,SECOND_SERVICE_NAME, "second-api", 8080, network)
                .dependsOn(postgreSQLContainer,activeMQ,eurekaServer);
        return this;
    }
    public ContainerConfigIntegrationTest startContainers() throws InterruptedException {
        postgreSQLContainer.start();
        activeMQ.start();
        eurekaServer.start();
        //Thread.sleep(15000);
        mainApi.start();
        secondApi.start();
        Thread.sleep(30000);
        //containerAreRunning = true;

        return this;
    }

    public void stopContainers() {
        secondApi.stop();
        mainApi.stop();
        eurekaServer.stop();
        activeMQ.stop();
        postgreSQLContainer.stop();
        network.close();
    }


    static private GenericContainer prepareTestContainer(File location, String filename, String netAlias, int port, Network net) {
        return new GenericContainer(new ImageFromDockerfile()
                .withFileFromFile(filename, new File(location, filename))
                .withDockerfileFromBuilder(builder -> builder
                        .from("openjdk:17-oracle")
                        .copy(filename, "/app/" + filename)
                        .entryPoint("java", "-Dspring.profiles.active=test", "-jar", "/app/" + filename)
                        .build()))
                .withExposedPorts(port).withNetwork(net).withNetworkAliases(netAlias);
    }
    /*@BeforeAll
    //@io.cucumber.java.BeforeAll
    static void startContainers() throws InterruptedException {
        if (containerAreRunning)
            return;
        postgreSQLContainer.start();
        activeMQ.start();
        eurekaServer.start();
        Thread.sleep(15000);
        mainApi.start();
        secondApi.start();
        Thread.sleep(30000);
        containerAreRunning = true;
    }

    @AfterAll
    //@io.cucumber.java.AfterAll
    static void stopContainers() {
        containerAreRunning = false;
        secondApi.stop();
        mainApi.stop();
        eurekaServer.stop();
        activeMQ.stop();
        postgreSQLContainer.stop();
        network.close();
    }*/


    /*
    @Autowired
     TestRestTemplate restTemplate;

    @Test
    void SimpleTest() throws InterruptedException {

        //Thread.sleep(30000);
        ResponseEntity<String> response;

        String url = "http://" + secondApi.getHost() + ":" + secondApi.getMappedPort(8080) + "/ping";
        response = restTemplate.getForEntity(url, String.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo("secondary pong");
        //Thread.sleep(100000000);
    }

    */


}
