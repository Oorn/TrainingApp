package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Mentor;
import org.example.exceptions.NoSuchEntityException;
import org.example.exceptions.RemovedEntityException;
import org.example.repository.MentorHibernateRepository;
import org.example.requests_responses.trainer.CreateMentorRequest;
import org.example.requests_responses.trainer.MentorFullInfoResponse;
import org.example.requests_responses.trainer.UpdateMentorProfileRequest;
import org.example.requests_responses.user.CredentialsResponse;
import org.example.service.CredentialsService;
import org.example.service.CredentialsServiceUtils;
import org.example.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MentorServiceImpl implements MentorService {
    @Setter(onMethod_={@Autowired})
    private CredentialsServiceUtils credentialsServiceUtils;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    //@Setter(onMethod_={@Autowired})
    //private TrainerRepository trainerRepository;
    @Autowired
    private MentorHibernateRepository mentorHibernateRepository;

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;
    @Override
    public CredentialsResponse create(CreateMentorRequest request) {
        String username = credentialsService.generateUsername(request.getFirstName(), request.getLastName());
        String password = credentialsServiceUtils.generateRandomPassword();

        Mentor newMentor = Objects.requireNonNull(converter.convert(request, Mentor.class));
        newMentor.getUser().setUserName(username);
        credentialsServiceUtils.setUserPassword(newMentor.getUser(),password);

        mentorHibernateRepository.saveAndFlush(newMentor);

        return CredentialsResponse.builder()
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public MentorFullInfoResponse get(String username) {
        Mentor mentor = mentorHibernateRepository.findMentorByUsername(username).orElse(null);
        if (mentor == null)
            throw new NoSuchEntityException("trainer not found");
        if (mentor.isRemoved())
            throw new RemovedEntityException("trainer has been removed");
        return converter.convert(mentor, MentorFullInfoResponse.class);
    }

    @Override
    public MentorFullInfoResponse update(String authUsername, UpdateMentorProfileRequest request) {
        Mentor mentor = mentorHibernateRepository.findMentorByUsername(authUsername).orElse(null);
        if (mentor == null)
            throw new NoSuchEntityException("trainer not found");
        if (mentor.isRemoved())
            throw new RemovedEntityException("trainer has been removed");

        mentor.getUser().setFirstName(request.getFirstName());
        mentor.getUser().setLastName(request.getLastName());
        mentor.getUser().setActive(request.isActive());
        if (request.isActive())
            mentor.getPartnerships().forEach(tp->tp.setRemoved(true)); //automatically remove partnerships of inactive users (but not trainings)

        mentorHibernateRepository.saveAndFlush(mentor);

        return converter.convert(mentor, MentorFullInfoResponse.class);
    }
}
