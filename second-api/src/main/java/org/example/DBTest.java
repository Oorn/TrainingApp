package org.example;

import org.example.repository.TrainingSummaryRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

//@Component
public class DBTest {
    @Autowired
    TrainingSummaryRepository repository;

    @PostConstruct
    public void postConstruct() {
        TrainingSummaryEntity entity = TrainingSummaryEntity.builder()
                .username("username")
                .isActive(true)
                .firstName("firstname")
                .lastName("lastname")
                .build();
        System.out.println("SAVING");
        repository.save(entity);
    }
}
