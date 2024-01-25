package org.example.repository.impl.v2.hibernate;

import org.example.domain_entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerHibernateRepository extends CrudRepository<Trainer, Long>
        , JpaRepository<Trainer, Long>
        , PagingAndSortingRepository<Trainer, Long> {

    @Query(value = "select t from Trainer t" +
            " left join fetch t.user u" +
            " where u.userName = :username")
    Optional<Trainer> findTrainerByUsername(String username);
}
