package org.example.converters;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain_entities.Mentor;
import org.example.requests_responses.trainee.StudentShortInfoResponse;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MentorFullInfoResponseConverter implements Converter<Mentor, MentorFullInfoResponse> {

    @Setter(onMethod_={@Autowired, @Lazy})
    private ConversionService converter;

    @Override
    public MentorFullInfoResponse convert(Mentor mentor) {
        return MentorFullInfoResponse.builder()
                .specialization(mentor.getSpecialization().getSpecialisationName())
                .firstName(mentor.getUser().getFirstName())
                .lastName(mentor.getUser().getLastName())
                .username(mentor.getUser().getUserName())
                .isActive(mentor.getUser().isActive())
                .studentsList(mentor.getPartnerships().stream()
                        .filter(t -> !t.isRemoved())
                        .filter(t -> t.getStudent().getUser().isActive())
                        .map(p->converter.convert(p.getStudent(), StudentShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}

