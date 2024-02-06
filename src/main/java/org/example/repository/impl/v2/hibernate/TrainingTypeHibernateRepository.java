package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeHibernateRepository extends JpaRepository<TrainingType, Long> {
    Optional<TrainingType> findTrainingTypeByTrainingType(String trainingType);
}
