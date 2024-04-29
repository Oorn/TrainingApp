package org.example.to_externalize.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingDurationSummaryResponse {
    private String executionStatus;

    private String username;

    private String name;

    private String surname;

    private Duration duration;

    private boolean active;


    public static final String STATUS_FOUND = "STATUS_FOUND";
    public static final String STATUS_NOT_FOUND = "STATUS_NOT_FOUND";
    public static final String STATUS_BAD_REQUEST = "STATUS_BAD_REQUEST";
    public static final String STATUS_SERVICE_DOWN = "STATUS_SERVICE_DOWN";
}
