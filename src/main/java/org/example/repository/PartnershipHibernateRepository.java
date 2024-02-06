package org.example.repository;

import org.example.domain_entities.Partnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartnershipHibernateRepository extends JpaRepository<Partnership, Long> {

    @Query(value = "select p from Partnership p" +
            " left join fetch p.student s" +
            " left join fetch s.user su" +
            " where su.userName = :traineeName")
    List<Partnership> findPartnershipByTraineeUsername(String traineeName);

    @Query(value = "select p from Partnership p" +
            " left join fetch p.mentor m" +
            " left join fetch m.user mu" +
            " where mu.userName = :trainerName")
    List<Partnership> findPartnershipByTrainerUsername(String trainerName);

    @Query(value = "select p from Partnership p" +
            " left join fetch p.mentor m" +
            " left join fetch m.user mu" +
            " left join fetch p.student s" +
            " left join fetch s.user su" +
            " where su.userName = :trainerName and mu.userName = :traineeName")
    Optional<Partnership> findPartnershipByTraineeTrainerUsernames(String traineeName, String trainerName);
}
