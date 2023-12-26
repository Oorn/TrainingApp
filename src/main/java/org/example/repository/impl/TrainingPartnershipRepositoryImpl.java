package org.example.repository.impl;

import lombok.Setter;
import org.apache.commons.collections.map.MultiKeyMap;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
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
    public Optional<TrainingPartnership> getByTraineeTrainer(String traineeName, String trainerName) {
        Set<TrainingPartnership> partnershipsByTrainee = mapByTrainee.get(traineeName);
        return partnershipsByTrainee.stream()
                .filter(t->t.getTrainer().getUser().getUserName().equals(trainerName))
                .filter(t->!t.isRemoved())
                .findAny();
    }

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
    public List<TrainingPartnership> updateAndReturnListForTrainee(String username, List<Trainer> trainers) {
        Trainee trainee = traineeRepository.get(username).orElseThrow();
        //should happen only on new trainee creation
        Set<TrainingPartnership> oldMapValue = mapByTrainee.computeIfAbsent(username, k -> new HashSet<>());

        //remove partnerships that are no longer present
        oldMapValue.stream()
                .filter(tp -> !trainers.contains(tp.getTrainer()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        oldMapValue.stream()
                .filter(tp -> trainers.contains(tp.getTrainer()))
                .forEach( tp -> {
                    tp.setRemoved(false);
                    trainers.remove(tp.getTrainer()); //so that trainers only contains trainers whose partnerships we still need to create
                });
        //add new partnerships

        trainers.stream()
                .map(t -> TrainingPartnership.builder()
                        .trainee(trainee)
                        .trainer(t)
                        .isRemoved(false)
                        .trainings(new HashSet<>())
                        .build())
                .collect(Collectors.toList()).forEach(this::save); //collect is present so that all partnerships can throw exceptions before saving starts
        //todo - remove collection trickery as mapping can no longer throw exceptions
        return getByTraineeName(username);

    }

    @Override
    public List<TrainingPartnership> updateAndReturnListForTrainer(String username, List<Trainee> trainees) {
        Trainer trainer = trainerRepository.get(username).orElseThrow();
        //should happen only on new trainee creation
        Set<TrainingPartnership> oldMapValue = mapByTrainer.computeIfAbsent(username, k -> new HashSet<>());

        //remove partnerships that are no longer present
        oldMapValue.stream()
                .filter(tp -> !trainees.contains(tp.getTrainee()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        oldMapValue.stream()
                .filter(tp -> trainees.contains(tp.getTrainee()))
                .forEach( tp -> {
                    tp.setRemoved(false);
                    trainees.remove(tp.getTrainee()); //so that trainers only contains trainers whose partnerships we still need to create
                });
        //add new partnerships
        trainees.stream()
                .map(t -> TrainingPartnership.builder()
                        .trainee(t)
                        .trainer(trainer)
                        .isRemoved(false)
                        .trainings(new HashSet<>())
                        .build()).
                collect(Collectors.toList()).forEach(this::save);//collect is present so that all partnerships can throw exceptions before saving starts
        //todo - remove collection trickery as mapping can no longer throw exceptions
        return getByTrainerName(username);
    }

    @Override
    public TrainingPartnership save(TrainingPartnership entity) {
        if (trainerRepository.get(entity.getTrainer().getUser().getUserName()).isEmpty())
            return null;
        if (traineeRepository.get(entity.getTrainee().getUser().getUserName()).isEmpty())
            return null;
        entity.getTrainer().getTrainingPartnerships().add(entity);
        entity.getTrainee().getTrainingPartnerships().add(entity);
        entity.setId(idProvider.provideIdentity(TrainingPartnership.class));
        mapByTrainer.computeIfAbsent(entity.getTrainer().getUser().getUserName(), k -> new HashSet<>()).add(entity);
        mapByTrainee.computeIfAbsent(entity.getTrainee().getUser().getUserName(), k -> new HashSet<>()).add(entity);
        return entity;
    }

    /*@Override
    public void deleteAllForTrainee(String username) {
        updateAndReturnListForTrainee(username, new ArrayList<>());
    }
    @Override
    public void deleteAllForTrainer(String username) {
        updateAndReturnListForTrainer(username, new ArrayList<>());
    }*/
}
