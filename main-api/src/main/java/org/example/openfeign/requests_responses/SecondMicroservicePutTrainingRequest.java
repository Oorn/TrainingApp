package org.example.openfeign.requests_responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.Duration;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecondMicroservicePutTrainingRequest {
    private String username;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private Timestamp date;

    private Duration duration;

    private String action;

    public static final String ACTION_ADD = "ACTION_ADD";
    public static final String ACTION_DELETE = "STATUS_NOT_FOUND";
}
