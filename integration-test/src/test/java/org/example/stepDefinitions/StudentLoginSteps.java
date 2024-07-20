package org.example.stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.TestContainersEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class StudentLoginSteps {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateStudentRequest {
        private String firstName;
        private String lastName;
        private String dateOfBirth;
        private String address;

    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CredentialsResponse {
        private String username;
        private String password;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MentorShortInfoResponse {
        private String username;
        private String firstName;
        private String lastName;
        private String specialization;
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentFullInfoResponse {
        private String username;
        private String firstName;
        private String lastName;
        private Timestamp dateOfBirth;
        private String address;
        private boolean isActive;
        private List<MentorShortInfoResponse> mentorsList;

    }

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TestContainersEnvironment containers;

    String lastResponseString;
    HttpStatus lastResponseStatus;

    Object lastResponseObject;

    String name, surname, address, username, password, access_token;

    private void recordResponse (ResponseEntity<?> response) {
        assertThat(response).isNotNull();
        lastResponseObject = response.getBody();
        lastResponseString = lastResponseObject != null ? lastResponseObject.toString() : "";
        lastResponseStatus = response.getStatusCode();
    }

    @Given("new student {string} has completed registration")
    public void newStudentHasCompletedRegistration(String arg0) {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student";

        name = arg0;
        surname = "Surname";
        address = "Some address";
        CreateStudentRequest request = new CreateStudentRequest(name, surname, "2008-11-30", address);
        ResponseEntity<CredentialsResponse> response = restTemplate.postForEntity(url, request, CredentialsResponse.class);
        recordResponse(response);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert response.hasBody();
        username = response.getBody().username;
        password = response.getBody().password;
    }

    @When("logging in with incorrect password")
    public void loggingInWithIncorrectPassword() {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student/" + username + "/login";
        String wrong_password = "dfsdfsgdsdg";
        ResponseEntity<String> response = restTemplate.postForEntity(url, wrong_password, String.class);
        recordResponse(response);
    }

    @Then("access is denied as {string}")
    public void accessIsDenied(String arg0) {
        assertThat(lastResponseStatus).isEqualTo(HttpStatus.valueOf(arg0));
    }

    @When("logging in with correct password")
    public void loggingInWithCorrectPassword() {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student/" + username + "/login";
        ResponseEntity<String> response = restTemplate.postForEntity(url, password, String.class);
        recordResponse(response);
    }

    @Then("access token is returned")
    public void accessTokenIsReturned() {
        assertThat(lastResponseStatus).isEqualTo(HttpStatus.OK);
        access_token = lastResponseString;
    }

    @When("request user information without access token")
    public void requestUserInformationWithoutAccessToken() {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student/" + username;
        ResponseEntity<String> response = restTemplate.postForEntity(url, password, String.class);
        recordResponse(response);
    }

    @When("request user information with invalid access token")
    public void requestUserInformationWithInvalidAccessToken() {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student/" + username;


        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Token", access_token + "a");
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        recordResponse(response);
    }

    @When("request user information with valid access token")
    public void requestUserInformationWithValidAccessToken() {
        String url = "http://" + containers.mainApi.getHost() + ":" + containers.mainApi.getMappedPort(8080) + "/student/" + username;


        HttpHeaders headers = new HttpHeaders();
        headers.set("Auth-Token", access_token);
        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<StudentFullInfoResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, StudentFullInfoResponse.class);
        recordResponse(response);
    }

    @Then("user information is returned")
    public void userInformationIsReturned() {
        assertThat(lastResponseStatus).isEqualTo(HttpStatus.OK);
        assertThat(lastResponseObject).isInstanceOf(StudentFullInfoResponse.class);
        StudentFullInfoResponse response = (StudentFullInfoResponse) lastResponseObject;
        assertThat(response.firstName).isEqualTo(name);
        assertThat(response.lastName).isEqualTo(surname);
        assertThat(response.address).isEqualTo(address);
        assert response.isActive;

        System.out.println("valid user registration test: " + lastResponseString);
    }
}
