package org.example.repository.impl;

import lombok.Setter;
import org.example.domain_entities.Training;
import org.example.repository.TrainingRepository;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.service.IdentityProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class TrainingRepositoryImpl implements TrainingRepository {

    @Setter(onMethod_={@Autowired})
    private IdentityProviderService idProvider;

    private final TreeMap<String, Set<Training>> mapByTrainer = new TreeMap<>();

    private final TreeMap<String, Set<Training>> mapByTrainee = new TreeMap<>();

    @Override
    public Training save(Training training) { //does not check for trainingPartnership validity
        training.setId(idProvider.provideIdentity(Training.class));
        training.getTrainingPartnership().getTrainings().add(training);//back link
        mapByTrainer.computeIfAbsent(training.getTrainingPartnership().getTrainer().getUser().getUserName(), k -> new HashSet<>()).add(training);
        mapByTrainee.computeIfAbsent(training.getTrainingPartnership().getTrainee().getUser().getUserName(), k -> new HashSet<>()).add(training);
        return training;
    }

    @Override
    public List<Training> getTrainingsByFilter(TrainingSearchFilter searchFilter) {
        Stream<Training> stream;
        if (searchFilter.getTraineeName() != null)
            stream = mapByTrainee.get(searchFilter.getTraineeName()).stream();
        else if (searchFilter.getTrainerName() != null)
            stream = mapByTrainer.get(searchFilter.getTrainerName()).stream();
        else
            stream = mapByTrainee.values().stream().flatMap(Collection::stream);
        //initial big grained stream setup complete, now filtering.
        //there might be some redundant checks to keep the code clean
        //stream = stream.filter(t->!t.isRemoved()); //removed handling moved to service
        if (searchFilter.getTraineeName() != null)
            stream = stream.filter(t->t.getTrainingPartnership().getTrainee().getUser().getUserName().equals(searchFilter.getTraineeName()));
        if (searchFilter.getTrainerName() != null)
            stream = stream.filter(t->t.getTrainingPartnership().getTrainer().getUser().getUserName().equals(searchFilter.getTrainerName()));
        if (searchFilter.getTrainingType() != null)
            stream = stream.filter(t->t.getTrainingPartnership().getTrainer().getSpecialization().getTrainingType().equals(searchFilter.getTrainingType()));
        if (searchFilter.getDateFrom() != null)
            stream = stream.filter(t->t.getTrainingDateTo().isAfter(searchFilter.getDateFrom()));
        if (searchFilter.getDateTo() != null)
            stream = stream.filter(t->t.getTrainingDateFrom().isBefore(searchFilter.getDateTo()));

        return stream.collect(Collectors.toList());
    }

}
