package org.example.repository;

import org.example.domain_entities.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MentorHibernateRepository extends JpaRepository<Mentor, Long> {

    @Query(value = "select m from Mentor m" +
            " left join fetch m.user u" +
            " where u.userName = :username")
    Optional<Mentor> findMentorByUsername(String username);
}
