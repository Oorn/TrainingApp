package org.example.repository;

import org.example.domain_entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentHibernateRepository extends JpaRepository<Student, Long> {

    @Query(value = "select s from Student s" +
            " left join fetch s.user u" +
            " where u.userName = :username")
    Optional<Student> findStudentByUsername(String username);

}
