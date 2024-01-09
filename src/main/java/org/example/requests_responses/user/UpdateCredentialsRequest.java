package org.example.requests_responses.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
public class UpdateCredentialsRequest {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String username;

    String oldPassword;

    String newPassword;
}
