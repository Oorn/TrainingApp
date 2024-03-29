package org.example.repository;

import org.example.domain_entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserHibernateRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUserName(String userName);
    List<User> findUsersByUserNameIsStartingWith(String prefix);
}
