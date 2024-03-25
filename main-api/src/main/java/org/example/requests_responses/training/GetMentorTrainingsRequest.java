package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.validation.ValidationConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetMentorTrainingsRequest {

    //@Schema(accessMode = Schema.AccessMode.READ_ONLY,
    //        description = "IGNORED. Comment - redundant and superseded by matching path variable. Cannot be hidden due to https://github.com/swagger-api/swagger-ui/issues/7696")
    //private String username;

    @Size(max = ValidationConstants.MAX_USERNAME_LENGTH)
    private String studentUsername;

    @Schema(defaultValue = "2023-05-01 15:23:45")
    private Timestamp dateFrom;

    @Schema(defaultValue = "2023-05-01 15:23:45")
    private Timestamp dateTo;
}
