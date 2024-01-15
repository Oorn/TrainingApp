package org.example.converters;

import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingType;
import org.example.domain_entities.User;
import org.example.repository.TrainingTypeRepository;
import org.example.requests_responses.trainer.CreateTrainerRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateTrainerRequestConverterTest {

    @Mock
    private TrainingTypeRepository trainingTypeRepository;
    @InjectMocks
    CreateTrainerRequestConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String specialisation = "specialisation";
        TrainingType trainingType = TrainingType.builder().trainingType(specialisation).build();

        Mockito.when(trainingTypeRepository.get(specialisation)).thenReturn(Optional.ofNullable(trainingType));

        CreateTrainerRequest request = CreateTrainerRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .specialisation(specialisation)
                .build();

        Trainer trainer = converter.convert(request);
        assert trainer != null;
        assert !trainer.isRemoved();
        assertEquals(trainingType, trainer.getSpecialization());

        User user = trainer.getUser();
        assertEquals(trainer, user.getTrainerProfile());
        assert user.isActive();
        assert !user.isRemoved();
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

}