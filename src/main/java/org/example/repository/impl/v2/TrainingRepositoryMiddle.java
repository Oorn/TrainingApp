package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.Training;
import org.example.repository.TrainingRepository;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.repository.impl.v2.hibernate.TrainingHibernateRepository;
import org.example.repository.impl.v2.hibernate.TrainingPartnershipHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Primary
@Deprecated
public class TrainingRepositoryMiddle implements TrainingRepository {
    @Setter(onMethod_={@Autowired})
    private TrainingHibernateRepository hibernateTraining;

    @Override
    public Training save(Training training) {
        return hibernateTraining.save(training);
    }

    @Override
    public List<Training> getTrainingsByFilter(TrainingSearchFilter searchFilter) {
        return hibernateTraining.findTrainingByFilter(searchFilter);
    }
}
