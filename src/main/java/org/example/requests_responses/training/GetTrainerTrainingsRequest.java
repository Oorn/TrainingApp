package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTrainerTrainingsRequest {

    //@Schema(accessMode = Schema.AccessMode.READ_ONLY,
    //        description = "IGNORED. Comment - redundant and superseded by matching path variable. Cannot be hidden due to https://github.com/swagger-api/swagger-ui/issues/7696")
    //private String username;

    private String traineeUsername;

    @Schema(defaultValue = "2023-05-01 15:23:45")
    private Timestamp dateFrom;

    @Schema(defaultValue = "2023-05-01 15:23:45")
    private Timestamp dateTo;
}
