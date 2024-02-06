package org.example.repository.impl.v2;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.repository.TraineeRepository;
import org.example.repository.impl.v2.hibernate.TraineeHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Primary
@Deprecated
public class TraineeRepositoryMiddle implements TraineeRepository {
    @Setter(onMethod_={@Autowired})
    private TraineeHibernateRepository hibernateTrainee;

    @Override
    public Optional<Trainee> get(String username) {
        return hibernateTrainee.findTraineeByUsername(username);
    }

    @Override
    public Trainee save(Trainee entity) {
        return hibernateTrainee.save(entity);
    }
}
