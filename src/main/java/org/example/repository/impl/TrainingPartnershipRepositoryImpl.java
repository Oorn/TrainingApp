package org.example.repository.impl;

import lombok.Setter;
import org.apache.commons.collections.map.MultiKeyMap;
import org.example.domain_entities.TrainingPartnership;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.TrainingRepository;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class TrainingPartnershipRepositoryImpl implements TrainingPartnershipRepository {

    @Setter(onMethod_={@Autowired, @Lazy})
    private TraineeRepository traineeRepository;

    @Setter(onMethod_={@Autowired, @Lazy})
    private TrainerRepository trainerRepository;

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;

    private final TreeMap<String, Set<TrainingPartnership>> mapByTrainer = new TreeMap<>();

    private final TreeMap<String, Set<TrainingPartnership>> mapByTrainee = new TreeMap<>();


    @Override
    public List<TrainingPartnership> getByTraineeName(String username) {
        Set<TrainingPartnership> mapValue = mapByTrainee.get(username);
        if (mapValue == null)
            return new ArrayList<>();
        return mapValue.stream()
                .filter(tp -> !tp.isRemoved())
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingPartnership> getByTrainerName(String username) {
        Set<TrainingPartnership> mapValue = mapByTrainer.get(username);
        if (mapValue == null)
            return new ArrayList<>();
        return mapValue.stream()
                .filter(tp -> !tp.isRemoved())
                .collect(Collectors.toList());
    }

    @Override
    public List<TrainingPartnership> updateAndReturnListForTrainee(String username, List<String> trainers) {
        //should happen only on new trainee creation
        Set<TrainingPartnership> oldMapValue = mapByTrainee.computeIfAbsent(username, k -> new HashSet<>());

        //remove partnerships that are no longer present
        oldMapValue.stream()
                .filter(tp -> !trainers.contains(tp.getTrainer().getUser().getUserName()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        oldMapValue.stream()
                .filter(tp -> trainers.contains(tp.getTrainer().getUser().getUserName()))
                .forEach( tp -> {
                    tp.setRemoved(false);
                    trainers.remove(tp.getTrainer().getUser().getUserName()); //so that trainers only contains trainers whose partnerships we still need to create
                });
        //add new partnerships
        trainers.forEach(t -> {
            TrainingPartnership tp = TrainingPartnership.builder()
                    .trainee(traineeRepository.get(username).orElseThrow())
                    .trainer(trainerRepository.get(t).orElseThrow())
                    .isRemoved(false)
                    .trainings(new HashSet<>())
                    .build();
            save(tp);

        });
        return getByTraineeName(username);

    }

    @Override
    public List<TrainingPartnership> updateAndReturnListForTrainer(String username, List<String> trainees) {
        //should happen only on new trainee creation
        Set<TrainingPartnership> oldMapValue = mapByTrainer.computeIfAbsent(username, k -> new HashSet<>());

        //remove partnerships that are no longer present
        oldMapValue.stream()
                .filter(tp -> !trainees.contains(tp.getTrainee().getUser().getUserName()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        oldMapValue.stream()
                .filter(tp -> trainees.contains(tp.getTrainee().getUser().getUserName()))
                .forEach( tp -> {
                    tp.setRemoved(false);
                    trainees.remove(tp.getTrainee().getUser().getUserName()); //so that trainers only contains trainers whose partnerships we still need to create
                });
        //add new partnerships
        trainees.forEach(t -> {
            TrainingPartnership tp = TrainingPartnership.builder()
                    .trainee(traineeRepository.get(t).orElseThrow())
                    .trainer(trainerRepository.get(username).orElseThrow())
                    .isRemoved(false)
                    .trainings(new HashSet<>())
                    .build();
            save(tp);

        });
        return getByTrainerName(username);
    }

    @Override
    public TrainingPartnership save(TrainingPartnership entity) {
        if (trainerRepository.get(entity.getTrainer().getUser().getUserName()).isEmpty())
            return null;
        if (traineeRepository.get(entity.getTrainee().getUser().getUserName()).isEmpty())
            return null;
        entity.setId(idProvider.provideIdentity(TrainingPartnership.class));
        mapByTrainer.computeIfAbsent(entity.getTrainer().getUser().getUserName(), k -> new HashSet<>()).add(entity);
        mapByTrainee.computeIfAbsent(entity.getTrainee().getUser().getUserName(), k -> new HashSet<>()).add(entity);
        return entity;
    }

    @Override
    public void deleteAllForTrainee(String username) {
        updateAndReturnListForTrainee(username, new ArrayList<>());
    }
    @Override
    public void deleteAllForTrainer(String username) {
        updateAndReturnListForTrainer(username, new ArrayList<>());
    }
}
