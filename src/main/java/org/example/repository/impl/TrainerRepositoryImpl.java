package org.example.repository.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.domain_entities.TrainingType;
import org.example.exceptions.IllegalStateException;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.UserRepository;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Repository
public class TrainerRepositoryImpl implements TrainerRepository {

    private final TreeMap<String, Trainer> TrainerMap = new TreeMap<>();

    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipRepository trainingPartnershipRepository;


    @Setter(onMethod_={@Autowired})
    private UserRepository userRepository;

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;

    @Override
    public Optional<Trainer> get(String username) {
        Trainer oldTrainer = TrainerMap.get(username);
        if (oldTrainer == null)
            return Optional.empty();
        return Optional.of(oldTrainer);
    }

    @Override
    public List<Trainer> getAll() {
        return TrainerMap.values().stream()
                .filter(t-> !t.isRemoved())
                .collect(Collectors.toList());
    }

    @Override
    public Trainer save(Trainer entity) {
        Trainer oldTrainer = TrainerMap.get(entity.getUser().getUserName());
        if (oldTrainer == null) {
            //new user case
            if (!entity.getTrainingPartnerships().isEmpty())
                //illegal state, creating user with non-empty partnerships
                throw new IllegalStateException("error - attempting to create trainer with non-empty partnerships");
            entity.setId(idProvider.provideIdentity(Trainer.class));
            entity.getSpecialization().getTrainers().add(entity);
            TrainerMap.put(entity.getUser().getUserName(), entity);
            entity.getUser().setTrainerProfile(entity);//back link
            userRepository.save(entity.getUser());
            trainingPartnershipRepository.updateAndReturnListForTrainer(entity.getUser().getUserName(), new ArrayList<>());
            return entity;
        }
        if (oldTrainer == entity) {
            //do nothing because entity updates are automatically synced to repository
            return entity;
        }
        // illegal state, entity and oldTrainer have same username but different objects
        throw new IllegalStateException("error - users with duplicate usernames");
    }

}
