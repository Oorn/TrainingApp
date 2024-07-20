package org.example.stepDefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.example.TestContainersEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class PingSteps {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestContainersEnvironment containers;

    ResponseEntity<String> lastResponse;

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
