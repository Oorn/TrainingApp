package org.example.requests_responses.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCredentialsRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY)
    //String username;

    @NotBlank
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
    String oldPassword;

    @NotBlank
    @Size(min = ValidationConstants.MIN_PASSWORD_LENGTH, max = ValidationConstants.MAX_PASSWORD_LENGTH)
    String newPassword;
}
