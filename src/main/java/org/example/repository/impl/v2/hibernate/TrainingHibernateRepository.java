package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.Training;
import org.example.repository.dto.TrainingSearchFilter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TrainingHibernateRepository extends JpaRepository<Training, Long> {

    @Query(value = "select t from Training t" +
            " left join fetch t.trainingPartnership p" +
            " left join fetch p.trainee te" +
            " left join fetch te.user teu" +
            " left join fetch p.trainer tr" +
            " left join fetch tr.user tru" +
            " left join fetch tr.specialization s" +
            " where (:#{#filter.traineeName} is null or teu.userName = :#{#filter.traineeName})" +
            " and (:#{#filter.trainerName} is null or tru.userName = :#{#filter.trainerName})" +
            " and (:#{#filter.trainingType} is null or s.trainingType = :#{#filter.trainingType})" +
            " and (cast(cast(:#{#filter.dateFrom} as text) as timestamp) is null or t.trainingDateTo > :#{#filter.dateFrom})" +
            " and (cast(cast(:#{#filter.dateTo} as text) as timestamp) is null or t.trainingDateFrom < :#{#filter.dateTo})")
    List<Training> findTrainingByFilter(@Param("filter") TrainingSearchFilter filter);
}
