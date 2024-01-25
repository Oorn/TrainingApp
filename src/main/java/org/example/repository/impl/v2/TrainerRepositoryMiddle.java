package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.Trainer;
import org.example.repository.TrainerRepository;
import org.example.repository.impl.v2.hibernate.TrainerHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Primary
public class TrainerRepositoryMiddle implements TrainerRepository {
    @Setter(onMethod_={@Autowired})
    private TrainerHibernateRepository hibernateTrainer;
    @Override
    public Optional<Trainer> get(String username) {
        return hibernateTrainer.findTrainerByUsername(username);
    }

    @Override
    public List<Trainer> getAll() {
        return hibernateTrainer.findAll();
    }

    @Override
    public Trainer save(Trainer entity) {
        return hibernateTrainer.save(entity);
    }
}
