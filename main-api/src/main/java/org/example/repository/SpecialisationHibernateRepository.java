package org.example.repository;

import org.example.domain_entities.Specialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialisationHibernateRepository extends JpaRepository<Specialisation, Long> {
    Optional<Specialisation> findSpecialisationBySpecialisationName(String specialisationName);
}
