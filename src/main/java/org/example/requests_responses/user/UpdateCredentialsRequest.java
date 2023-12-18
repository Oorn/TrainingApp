package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
public class UpdateCredentialsRequest {
    String username;

    String oldPassword;

    String newPassword;
}
