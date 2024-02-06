package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.TrainingType;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.impl.v2.hibernate.TrainingTypeHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
@Deprecated
public class TrainingTypeRepositoryMiddle implements TrainingTypeRepository {
    @Setter(onMethod_={@Autowired})
    private TrainingTypeHibernateRepository hibernateType;
    @Override
    public Optional<TrainingType> get(String name) {
        return hibernateType.findTrainingTypeByTrainingType(name);
    }

    @Override
    public List<TrainingType> getAll() {
        return hibernateType.findAll();
    }

    @Override
    public TrainingType create(String name) {
        TrainingType newType = TrainingType.builder().trainingType(name).build();
        return hibernateType.save(newType);
    }
}
