package org.example.requests_responses.training;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.Instant;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetTraineeTrainingsRequest {
    //@Schema(accessMode = Schema.AccessMode.READ_ONLY,
    //        description = "IGNORED. Comment - redundant and superseded by matching path variable. Cannot be hidden due to https://github.com/swagger-api/swagger-ui/issues/7696")
    //private String username;

    private String trainerUsername;

    private String type;

    @Schema(defaultValue = "2018-11-30T18:35:24.00Z")
    private Instant dateFrom;

    @Schema(defaultValue = "2018-11-30T18:35:24.00Z")
    private Instant dateTo;
}
