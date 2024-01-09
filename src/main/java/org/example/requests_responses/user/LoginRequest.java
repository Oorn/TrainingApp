package org.example.requests_responses.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
public class LoginRequest {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String username;

    private String password;
}
