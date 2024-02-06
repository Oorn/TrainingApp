package org.example.converters;

import lombok.RequiredArgsConstructor;

import org.example.domain_entities.Student;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class CreateStudentRequestConverter implements Converter<CreateStudentRequest, Student> {


    @Override
    public Student convert(CreateStudentRequest request) {

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        Student newStudent = Student.builder()
                .address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .partnerships(new HashSet<>())
                .user(newUser)
                .isRemoved(false)
                .build();
        newUser.setStudentProfile(newStudent);
        return newStudent;
    }
}
