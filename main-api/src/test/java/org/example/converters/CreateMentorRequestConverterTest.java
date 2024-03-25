package org.example.converters;

import org.example.domain_entities.Mentor;
import org.example.domain_entities.Specialisation;
import org.example.domain_entities.User;
import org.example.repository.SpecialisationHibernateRepository;
import org.example.requests_responses.trainer.CreateMentorRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreateMentorRequestConverterTest {

    @Mock
    private SpecialisationHibernateRepository trainingTypeRepository;
    @InjectMocks
    CreateMentorRequestConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String specialisation = "specialisation";
        Specialisation trainingType = Specialisation.builder().specialisationName(specialisation).build();

        Mockito.when(trainingTypeRepository.findSpecialisationBySpecialisationName(specialisation)).thenReturn(Optional.ofNullable(trainingType));

        CreateMentorRequest request = CreateMentorRequest.builder()
                .firstName(firstName)
                .lastName(lastName)
                .specialisation(specialisation)
                .build();

        Mentor mentor = converter.convert(request);
        assert mentor != null;
        assert !mentor.isRemoved();
        assertEquals(trainingType, mentor.getSpecialization());

        User user = mentor.getUser();
        assertEquals(mentor, user.getMentorProfile());
        assert user.isActive();
        assert !user.isRemoved();
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

}