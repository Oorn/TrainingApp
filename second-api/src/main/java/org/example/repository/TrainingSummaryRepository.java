package org.example.repository;

import org.example.TrainingSummaryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainingSummaryRepository extends JpaRepository<TrainingSummaryEntity, Long> {
    Optional<TrainingSummaryEntity> findTrainingSummaryEntityByMonthAndUsername(Timestamp month, String username);
    List<TrainingSummaryEntity> findAllByUsername(String username);
}
