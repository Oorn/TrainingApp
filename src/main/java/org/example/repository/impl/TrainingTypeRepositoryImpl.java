package org.example.repository.impl;

import lombok.Setter;
import org.example.domain_entities.TrainingType;
import org.example.domain_entities.User;
import org.example.repository.TrainingTypeRepository;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrainingTypeRepositoryImpl implements TrainingTypeRepository {

    private final TreeMap<String, TrainingType> trainingTypeMap = new TreeMap<>();

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;

    @Override
    public Optional<TrainingType> get(String name) {
        TrainingType result = trainingTypeMap.get(name);
        if (result == null)
            return Optional.empty();
        return Optional.of(result);
    }

    @Override
    public List<TrainingType> getAll() {
        return new ArrayList<>(trainingTypeMap.values());
    }

    @Override
    public TrainingType create(String name) {
        Optional<TrainingType> oldTrainingType = get(name);
        if (oldTrainingType.isPresent())
            return oldTrainingType.get();
        TrainingType newTrainingType = TrainingType.builder()
                .trainingType(name)
                .trainers(new HashSet<>())
                .id(idProvider.provideIdentity(TrainingType.class))
                .build();
        trainingTypeMap.put(name,newTrainingType);
        return newTrainingType;
    }

}
