package org.example.service;

import org.example.requests_responses.trainee.CreateStudentRequest;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainee.UpdateStudentProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;

public interface StudentService {
    CredentialsResponse create (CreateStudentRequest request);
    StudentFullInfoResponse get(String username);
    StudentFullInfoResponse update(String authUsername, UpdateStudentProfileRequest request);
    boolean delete(String username);

    boolean isStudent(String username);

}
