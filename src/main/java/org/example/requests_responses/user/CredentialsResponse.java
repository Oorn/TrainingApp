package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CredentialsResponse {
    private String username;
    
    private String password;
}
