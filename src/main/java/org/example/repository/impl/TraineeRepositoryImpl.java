package org.example.repository.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.TrainingPartnership;
import org.example.domain_entities.User;
import org.example.exceptions.IllegalStateException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.UserRepository;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
public class TraineeRepositoryImpl implements TraineeRepository {

    private final TreeMap<String, Trainee> TraineeMap = new TreeMap<>();

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;

    @Setter(onMethod_={@Autowired})
    private UserRepository userRepository;

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;
    @Override
    public Optional<Trainee> get(String username) {
        Trainee oldTrainee = TraineeMap.get(username);
        if (oldTrainee == null)
            return Optional.empty();
        return Optional.of(oldTrainee);
    }

    @Override
    public Trainee save(Trainee entity) {
        Trainee oldTrainee = TraineeMap.get(entity.getUser().getUserName());
        if (oldTrainee == null) {
            //new user case
            if (!entity.getTrainingPartnerships().isEmpty())
                //illegal state, creating user with non-empty partnerships
                throw new IllegalStateException("error - attempting to create trainee with non-empty partnerships");
            entity.setId(idProvider.provideIdentity(Trainee.class));
            TraineeMap.put(entity.getUser().getUserName(), entity);
            entity.getUser().setTraineeProfile(entity);//back link
            userRepository.save(entity.getUser());
            trainingPartnershipRepository.updateAndReturnListForTrainee(entity.getUser().getUserName(), new ArrayList<>());
            return entity;
        }
        if (oldTrainee == entity) {
            //do nothing because entity updates are automatically synced to repository
            return entity;
        }
        // illegal state, entity and oldTrainee have same username but different objects
        throw new IllegalStateException("error - users with duplicate usernames");
    }
}
