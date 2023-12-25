package org.example.coverters;

import lombok.RequiredArgsConstructor;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.CreateTraineeRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class CreateTraineeRequestConverter implements Converter<CreateTraineeRequest, Trainee> {


    @Override
    public Trainee convert(CreateTraineeRequest request) {

        User newUser = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .isActive(true)
                .build();
        Trainee newTrainee = Trainee.builder()
                .Address(request.getAddress())
                .dateOfBirth(request.getDateOfBirth())
                .trainingPartnerships(new HashSet<>())
                .user(newUser)
                .isRemoved(false)
                .build();
        newUser.setTraineeProfile(newTrainee);
        return newTrainee;
    }
}
