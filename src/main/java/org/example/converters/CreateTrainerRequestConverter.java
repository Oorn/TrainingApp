package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.User;
import org.example.exceptions.NoSuchEntityException;
import org.example.repository.TrainingTypeHibernateRepository;
import org.example.requests_responses.trainer.CreateTrainerRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class CreateTrainerRequestConverter implements Converter<CreateTrainerRequest, Trainer> {

    //@Setter(onMethod_={@Autowired})
    //private TrainingTypeRepository trainingTypeRepository;
    @Autowired
    private TrainingTypeHibernateRepository trainingTypeHibernateRepository;

    @Override
    public Trainer convert(CreateTrainerRequest request) {
        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        Trainer newTrainer = Trainer.builder()
                .specialization(trainingTypeHibernateRepository.findTrainingTypeByTrainingType(request.getSpecialisation())
                        .orElseThrow(()->new NoSuchEntityException("training type \"" + request.getSpecialisation() + "\" not found")))
                .trainingPartnerships(new HashSet<>())
                .user(newUser)
                .isRemoved(false)
                .build();
        newUser.setTrainerProfile(newTrainer);
        return newTrainer;
    }
}
