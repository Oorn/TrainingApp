package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Mentor;
import org.example.domain_entities.User;
import org.example.exceptions.NoSuchEntityException;
import org.example.repository.SpecialisationHibernateRepository;
import org.example.requests_responses.trainer.CreateMentorRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class CreateMentorRequestConverter implements Converter<CreateMentorRequest, Mentor> {

    //@Setter(onMethod_={@Autowired})
    //private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private SpecialisationHibernateRepository specialisationHibernateRepository;

    @Override
    public Mentor convert(CreateMentorRequest request) {
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        Mentor newMentor = Mentor.builder()
                .specialization(specialisationHibernateRepository.findSpecialisationBySpecialisationName(request.getSpecialisation())
                        .orElseThrow(()->new NoSuchEntityException("training type \"" + request.getSpecialisation() + "\" not found")))
                .partnerships(new HashSet<>())
                .user(newUser)
                .isRemoved(false)
                .build();
        newUser.setMentorProfile(newMentor);
        return newMentor;
    }
}
