package org.example.converters;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain_entities.Student;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainer.MentorShortInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudentFullInfoResponseConverter implements Converter<Student, StudentFullInfoResponse> {

    @Setter(onMethod_={@Autowired, @Lazy})
    private ConversionService converter;

    @Override
    public StudentFullInfoResponse convert(Student student) {
        return StudentFullInfoResponse.builder()
                .address(student.getAddress())
                .dateOfBirth(student.getDateOfBirth())
                .firstName(student.getUser().getFirstName())
                .lastName(student.getUser().getLastName())
                .username(student.getUser().getUserName())
                .isActive(student.getUser().isActive())
                .mentorsList(student.getPartnerships().stream()
                        .filter(t -> !t.isRemoved())
                        .filter(t -> t.getMentor().getUser().isActive())
                        .map(p->converter.convert(p.getMentor(), MentorShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
