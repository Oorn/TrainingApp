package org.example.service;

import org.example.domain_entities.TrainingType;

import java.util.List;

public interface TrainingTypeService {
    boolean create (String trainingType);
    List<String> get();
}
