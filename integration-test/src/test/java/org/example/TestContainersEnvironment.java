package org.example;


import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.builder.ImageFromDockerfile;


import java.io.File;

public class TestContainersEnvironment {

    private final static File SECOND_SERVICE_LOCATION = new File("../second-api/target");
    private final static File MAIN_SERVICE_LOCATION = new File("../main-api/target");

    private final static File EUREKA_SERVICE_LOCATION = new File("../eureka/target");
    private final static String SECOND_SERVICE_NAME = "TrainingApp-second.jar";
    private final static String MAIN_SERVICE_NAME = "TrainingApp-main.jar";

    private final static String EUREKA_SERVICE_NAME = "TrainingApp-eureka.jar";



    public Network network;
    public GenericContainer eurekaServer;
    public GenericContainer activeMQ;
    public GenericContainer postgreSQLContainer;
    public GenericContainer mainApi;
    public GenericContainer secondApi;

    public boolean containersAreRunning = false;

    public TestContainersEnvironment prepareContainers () {
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
    public TestContainersEnvironment startContainers() throws InterruptedException {
        postgreSQLContainer.start();
        activeMQ.start();
        eurekaServer.start();
        mainApi.start();
        secondApi.start();
        Thread.sleep(50000);
        containersAreRunning = true;
        return this;
    }

    public void stopContainers() {
        secondApi.stop();
        mainApi.stop();
        eurekaServer.stop();
        activeMQ.stop();
        postgreSQLContainer.stop();
        network.close();
        containersAreRunning = false;
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

}
