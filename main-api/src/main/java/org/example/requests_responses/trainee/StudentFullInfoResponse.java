package org.example.requests_responses.trainee;

import lombok.*;
import org.example.requests_responses.trainer.MentorShortInfoResponse;

import java.sql.Timestamp;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentFullInfoResponse {
    private String username;

    private String firstName;

    private String lastName;

    private Timestamp dateOfBirth;

    private String address;

    private boolean isActive;

    private List<MentorShortInfoResponse> mentorsList;

}
