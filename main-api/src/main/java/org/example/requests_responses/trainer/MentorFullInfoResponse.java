package org.example.requests_responses.trainer;

import lombok.*;
import org.example.requests_responses.trainee.StudentShortInfoResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private String specialization;

    private boolean isActive;

    List<StudentShortInfoResponse> studentsList;

}
