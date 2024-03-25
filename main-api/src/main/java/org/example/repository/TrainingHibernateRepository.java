package org.example.repository;

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
            " left join fetch t.partnership p" +
            " left join fetch p.student s" +
            " left join fetch s.user su" +
            " left join fetch p.mentor m" +
            " left join fetch m.user mu" +
            " left join fetch m.specialization sp" +
            " where (:#{#filter.studentName} is null or su.userName = :#{#filter.studentName})" +
            " and (:#{#filter.mentorName} is null or mu.userName = :#{#filter.mentorName})" +
            " and (:#{#filter.specialisation} is null or sp.specialisationName = :#{#filter.specialisation})" +
            " and (cast(cast(:#{#filter.dateFrom} as text) as timestamp) is null or t.trainingDateTo > :#{#filter.dateFrom})" +
            " and (cast(cast(:#{#filter.dateTo} as text) as timestamp) is null or t.trainingDateFrom < :#{#filter.dateTo})")
    List<Training> findTrainingByFilter(@Param("filter") TrainingSearchFilter filter);
}
