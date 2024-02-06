package org.example.service;

import org.example.requests_responses.trainer.CreateMentorRequest;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.trainer.UpdateMentorProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;

public interface MentorService {
    CredentialsResponse create(CreateMentorRequest request);
    MentorFullInfoResponse get(String username);
    MentorFullInfoResponse update(String authUsername, UpdateMentorProfileRequest request);
}
