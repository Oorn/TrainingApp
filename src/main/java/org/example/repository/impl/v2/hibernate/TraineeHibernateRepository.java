package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeHibernateRepository extends JpaRepository<Trainee, Long> {

    @Query(value = "select t from Trainee t" +
            " left join fetch t.user u" +
            " where u.userName = :username")
    Optional<Trainee> findTraineeByUsername(String username);

}
