package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.exceptions.NoSuchEntityException;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.impl.v2.hibernate.TrainingPartnershipHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Deprecated
public class TrainingPartnershipRepositoryMiddle implements TrainingPartnershipRepository {
    @Setter(onMethod_={@Autowired})
    private TrainingPartnershipHibernateRepository hibernatePartnership;
    @Setter(onMethod_={@Autowired, @Lazy})
    private TraineeRepository traineeRepository;
    @Setter(onMethod_={@Autowired, @Lazy})
    private TrainerRepository trainerRepository;

    @Override
    public Optional<TrainingPartnership> getByTraineeTrainer(String traineeName, String trainerName) {
        return hibernatePartnership.findPartnershipByTraineeTrainerUsernames(traineeName, trainerName);
    }

    @Override
    public List<TrainingPartnership> getByTraineeName(String username) {
        return hibernatePartnership.findPartnershipByTraineeUsername(username);
    }

    @Override
    public List<TrainingPartnership> getByTrainerName(String username) {
        return hibernatePartnership.findPartnershipByTrainerUsername(username);
    }

    @Override
    public List<TrainingPartnership> updateAndReturnListForTrainee(String username, List<Trainer> trainers) {
        Trainee trainee = traineeRepository.get(username).orElseThrow(()->new NoSuchEntityException("trainee not found"));

        //remove partnerships that are no longer present
        trainee.getTrainingPartnerships().stream()
                .filter(tp -> !trainers.contains(tp.getTrainer()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        trainee.getTrainingPartnerships().stream()
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
                .forEach(this::save);
        return getByTraineeName(username);
    }

    @Override
    public List<TrainingPartnership> updateAndReturnListForTrainer(String username, List<Trainee> trainees) {
        Trainer trainer = trainerRepository.get(username).orElseThrow(()->new NoSuchEntityException("trainer not found"));

        //remove partnerships that are no longer present
        trainer.getTrainingPartnerships().stream()
                .filter(tp -> !trainees.contains(tp.getTrainee()))
                .forEach(tp -> tp.setRemoved(true));
        //update removed status of re-added partnerships
        trainer.getTrainingPartnerships().stream()
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
                        .build())
                .forEach(this::save);
        return getByTrainerName(username);
    }

    @Override
    public TrainingPartnership save(TrainingPartnership entity) {
        return hibernatePartnership.saveAndFlush(entity);
    }
}
