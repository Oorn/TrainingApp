package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Mentor;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MentorShortInfoResponseConverter implements Converter<Mentor, MentorShortInfoResponse> {
    @Override
    public MentorShortInfoResponse convert(Mentor mentor) {
        return MentorShortInfoResponse.builder()
                .firstName(mentor.getUser().getFirstName())
                .lastName(mentor.getUser().getLastName())
                .username(mentor.getUser().getUserName())
                .specialization(mentor.getSpecialization().getSpecialisationName())
                .build();
    }
}
