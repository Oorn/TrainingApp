package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Student;
import org.example.requests_responses.trainee.StudentShortInfoResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentShortInfoResponseConverter implements Converter<Student, StudentShortInfoResponse> {
    @Override
    public StudentShortInfoResponse convert(Student student) {
        return StudentShortInfoResponse.builder()
                .firstName(student.getUser().getFirstName())
                .lastName(student.getUser().getLastName())
                .username(student.getUser().getUserName())
                .build();
    }
}
