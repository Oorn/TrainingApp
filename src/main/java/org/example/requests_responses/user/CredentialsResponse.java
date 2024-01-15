package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsResponse {
    private String username;
    
    private String password;
}
