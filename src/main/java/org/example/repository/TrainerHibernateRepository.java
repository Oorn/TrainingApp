package org.example.repository;

import org.example.domain_entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerHibernateRepository extends JpaRepository<Trainer, Long> {

    @Query(value = "select t from Trainer t" +
            " left join fetch t.user u" +
            " where u.userName = :username")
    Optional<Trainer> findTrainerByUsername(String username);
}
