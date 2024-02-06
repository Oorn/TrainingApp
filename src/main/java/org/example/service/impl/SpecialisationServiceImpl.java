package org.example.service.impl;

import org.example.domain_entities.Specialisation;
import org.example.repository.SpecialisationHibernateRepository;
import org.example.service.SpecialisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SpecialisationServiceImpl implements SpecialisationService {
    //@Setter(onMethod_={@Autowired})
    //private TrainingTypeRepository trainingTypeRepository;

    @Autowired
    private SpecialisationHibernateRepository specialisationHibernateRepository;

    @Override
    public boolean create(String trainingType) {
        if (get().contains(trainingType))
            return false;
        specialisationHibernateRepository.saveAndFlush(Specialisation.builder()
                .specialisationName(trainingType)
                .build());
        return true;

    }

    @Override
    public List<String> get() {
        return specialisationHibernateRepository.findAll().stream()
                .map(Specialisation::getSpecialisationName)
                .collect(Collectors.toList());
    }
}
