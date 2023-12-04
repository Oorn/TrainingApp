package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    private String username;

    private String password;
}
