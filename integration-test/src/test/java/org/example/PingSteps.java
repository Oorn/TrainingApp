package org.example;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PingSteps {
    ResponseEntity<String> lastResponse;
    @Autowired
    private TestRestTemplate restTemplate;


    @Autowired
    private ContainerConfigIntegrationTest containers;

    //{
    //    try {
    //        containers = CucumberContext.containers();
    //    } catch (InterruptedException e) {
    //        throw new RuntimeException(e);
    //    }
    //}

    @When("main service receives Get on {string}")
    public void mainServiceReceivesGetOn(String arg0) {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + arg0;
        lastResponse = restTemplate.getForEntity(url, String.class);

    }

    @Then("response code is {int}, content is {string}")
    public void responseCodeIsContentIs(int arg0, String arg1) {
        assertThat(lastResponse.getStatusCode()).isEqualTo(HttpStatus.valueOf(arg0));
        assertThat(lastResponse.getBody()).isEqualTo(arg1);
    }
}
