package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Student;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.StudentHibernateRepository;
import org.example.requests_responses.trainee.CreateStudentRequest;
import org.example.requests_responses.trainee.StudentFullInfoResponse;
import org.example.requests_responses.trainee.UpdateStudentProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class StudentServiceImpl implements StudentService {

    @Setter(onMethod_={@Autowired})
    private CredentialsServiceUtils credentialsServiceUtils;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    //@Setter(onMethod_={@Autowired})
    //private TraineeRepository traineeRepository;
    @Autowired
    private StudentHibernateRepository studentHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;


    @Override
    public CredentialsResponse create(CreateStudentRequest request) {
        String username = credentialsService.generateUsername(request.getFirstName(), request.getLastName());
        String password = credentialsServiceUtils.generateRandomPassword();

        Student newStudent = Objects.requireNonNull(converter.convert(request, Student.class));
        newStudent.getUser().setUserName(username);
        credentialsServiceUtils.setUserPassword(newStudent.getUser(),password);

        studentHibernateRepository.saveAndFlush(newStudent);

        return CredentialsResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public StudentFullInfoResponse get(String username) {
        Student student = studentHibernateRepository.findStudentByUsername(username).orElse(null);
        if (student == null)
            throw new NoSuchEntityException("trainee not found");
        if (student.isRemoved())
            throw new RemovedEntityException("trainee has been removed");
        return converter.convert(student, StudentFullInfoResponse.class);
    }

    @Override
    public StudentFullInfoResponse update(String authUsername, UpdateStudentProfileRequest request) {
        Student student = studentHibernateRepository.findStudentByUsername(authUsername).orElse(null);
        if (student == null)
            throw new NoSuchEntityException("trainee not found");
        if (student.isRemoved())
            throw new RemovedEntityException("trainee has been removed");

        student.setAddress(request.getAddress());
        student.setDateOfBirth(request.getDateOfBirth());
        student.getUser().setFirstName(request.getFirstName());
        student.getUser().setLastName(request.getLastName());
        student.getUser().setActive(request.isActive());
        if (request.isActive())
            student.getPartnerships().forEach(tp->tp.setRemoved(true)); //automatically remove partnerships of inactive users (but not trainings)

        studentHibernateRepository.saveAndFlush(student);

        return converter.convert(student, StudentFullInfoResponse.class);
    }

    @Override
    public boolean delete(String username) {
        Student student = studentHibernateRepository.findStudentByUsername(username).orElse(null);
        if (student == null)
            throw new NoSuchEntityException("trainee not found");
        student.setRemoved(true);
        student.getUser().setRemoved(true);
        student.getPartnerships().forEach(t->t.setRemoved(true));
        student.getPartnerships().stream().
                flatMap(t->t.getTrainings().stream()).
                forEach(t->t.setRemoved(true));

        studentHibernateRepository.saveAndFlush(student);
        return true;
    }

    @Override
    public boolean isStudent(String username) {
        Student student = studentHibernateRepository.findStudentByUsername(username).orElse(null);
        if (student == null)
            return false;
        return !student.isRemoved();
    }
}
