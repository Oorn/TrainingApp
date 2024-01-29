package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.TrainingPartnership;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingPartnershipHibernateRepository extends CrudRepository<TrainingPartnership, Long>
        , JpaRepository<TrainingPartnership, Long>
        , PagingAndSortingRepository<TrainingPartnership, Long> {

    @Query(value = "select p from TrainingPartnership p" +
            " left join fetch p.trainee te" +
            " left join fetch te.user teu" +
            " where teu.userName = :traineeName")
    List<TrainingPartnership> findPartnershipByTraineeUsername(String traineeName);

    @Query(value = "select p from TrainingPartnership p" +
            " left join fetch p.trainer tr" +
            " left join fetch tr.user tru" +
            " where tru.userName = :trainerName")
    List<TrainingPartnership> findPartnershipByTrainerUsername(String trainerName);

    @Query(value = "select p from TrainingPartnership p" +
            " left join fetch p.trainer tr" +
            " left join fetch tr.user tru" +
            " left join fetch p.trainee te" +
            " left join fetch te.user teu" +
            " where tru.userName = :trainerName and teu.userName = :traineeName")
    Optional<TrainingPartnership> findPartnershipByTrainerTraineeUsernames(String traineeName, String trainerName);
}