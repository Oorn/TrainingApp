package org.example.requests_responses.user;

import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCredentialsRequest {
    String username;

    String oldPassword;

    String newPassword;
}
