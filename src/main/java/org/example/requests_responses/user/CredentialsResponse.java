package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
public class CredentialsResponse {
    private String username;
    
    private String password;
}
