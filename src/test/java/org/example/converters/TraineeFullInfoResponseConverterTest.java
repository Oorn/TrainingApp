package org.example.converters;

import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.domain_entities.User;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TraineeFullInfoResponseConverterTest {

    @Mock
    private ConversionService conversionService;
    @InjectMocks
    TraineeFullInfoResponseConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String userName = "username";
        String address = "address";

        boolean isRemoved = false;
        boolean isActive = true;
        Set<TrainingPartnership> partnerships = new HashSet<>();
        Instant dateOfBirth = Instant.parse("2018-11-30T18:35:24.00Z");
        TrainerShortInfoResponse trainerShortInfo = new TrainerShortInfoResponse();

        User user = User.builder()
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .isRemoved(isRemoved)
                .isActive(isActive)
                .build();
        Trainee trainee = Trainee.builder()
                .dateOfBirth(Timestamp.from(dateOfBirth))
                .isRemoved(isRemoved)
                .address(address)
                .trainingPartnerships(partnerships)
                .user(user)
                .build();
        user.setTraineeProfile(trainee);

        Trainer removedPartnershipTrainer = Trainer.builder().build();


        User trainerUser = Mockito.mock(User.class);
        Trainer trainer = Trainer.builder().user(trainerUser).build();
        Mockito.when(trainerUser.isActive()).thenReturn(true);

        User inactiveTrainerUser = Mockito.mock(User.class);
        Trainer inactiveTrainer = Trainer.builder().user(inactiveTrainerUser).build();
        Mockito.when(inactiveTrainerUser.isActive()).thenReturn(false);

        partnerships.add(TrainingPartnership.builder().id(1).isRemoved(true).trainee(trainee).trainer(removedPartnershipTrainer).build());
        partnerships.add(TrainingPartnership.builder().id(2).isRemoved(false).trainee(trainee).trainer(inactiveTrainer).build());
        partnerships.add(TrainingPartnership.builder().id(3).isRemoved(false).trainee(trainee).trainer(trainer).build());

        Mockito.when(conversionService.convert(trainer, TrainerShortInfoResponse.class)).thenReturn(trainerShortInfo);

        TraineeFullInfoResponse response = converter.convert(trainee);

        assert response != null;
        assertEquals(address, response.getAddress());
        assertEquals(dateOfBirth, response.getDateOfBirth().toInstant());
        assertEquals(firstName, response.getFirstName());
        assertEquals(isActive, response.isActive());
        assertEquals(lastName, response.getLastName());
        assertEquals(userName, response.getUsername());
        List<TrainerShortInfoResponse> trainersInfoList= response.getTrainersList();
        assertEquals(1, trainersInfoList.size());
        assertEquals(trainerShortInfo, trainersInfoList.get(0));
    }

}

