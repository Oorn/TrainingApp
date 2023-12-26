package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.TrainingType;
import org.example.repository.TrainingTypeRepository;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    @Setter(onMethod_={@Autowired})
    private TrainingTypeRepository trainingTypeRepository;

    @Override
    public boolean create(String trainingType) {
        return (trainingTypeRepository.create(trainingType) != null);
    }

    @Override
    public List<String> get() {
        return trainingTypeRepository.getAll().stream()
                .map(TrainingType::getTrainingType)
                .collect(Collectors.toList());
    }
}
