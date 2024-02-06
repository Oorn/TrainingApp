package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.TrainingType;
import org.example.repository.TrainingTypeRepository;
import org.example.repository.impl.v2.hibernate.TrainingTypeHibernateRepository;
import org.example.service.TrainingTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class TrainingTypeServiceImpl implements TrainingTypeService {
    //@Setter(onMethod_={@Autowired})
    //private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private TrainingTypeHibernateRepository trainingTypeHibernateRepository;

    @Override
    public boolean create(String trainingType) {
        if (get().contains(trainingType))
            return false;
        trainingTypeHibernateRepository.saveAndFlush(TrainingType.builder()
                .trainingType(trainingType)
                .build());
        return true;

    }

    @Override
    public List<String> get() {
        return trainingTypeHibernateRepository.findAll().stream()
                .map(TrainingType::getTrainingType)
                .collect(Collectors.toList());
    }
}
