package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TrainingHibernateRepository extends CrudRepository<Training, Long>
        , JpaRepository<Training, Long>
        , PagingAndSortingRepository<Training, Long> {

    @Query(value = "select t from Training t" +
            " left join fetch t.trainingPartnership p" +
            " left join fetch p.trainee te" +
            " left join fetch te.user teu" +
            " left join fetch p.trainer tr" +
            " left join fetch tr.user tru" +
            " left join fetch tr.specialization s" +
            " where (:traineeName is null or teu.userName = :traineeName)" +
            " and (:trainerName is null or tru.userName = :trainerName)" +
            " and (:trainingType is null or s.trainingType = :trainingType)" +
            " and (:endAfter is null or t.trainingDateTo > :endAfter)" +
            " and (:beginBefore is null or t.trainingDateFrom < :beginBefore)")
    List<Training> findTrainingByFilter(String traineeName, String trainerName, String trainingType, Timestamp endAfter, Timestamp beginBefore);
}
