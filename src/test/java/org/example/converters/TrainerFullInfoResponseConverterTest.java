package org.example.converters;

import org.example.domain_entities.*;
import org.example.requests_responses.trainee.TraineeShortInfoResponse;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TrainerFullInfoResponseConverterTest {

    @Mock
    private ConversionService conversionService;
    @InjectMocks
    TrainerFullInfoResponseConverter converter;

    @Test
    void convert() {
        String firstName = "firstName";
        String lastName = "lastName";
        String userName = "username";
        String type = "type";
        TrainingType trainingType = TrainingType.builder().trainingType(type).build();

        boolean isRemoved = false;
        boolean isActive = true;
        Set<TrainingPartnership> partnerships = new HashSet<>();
        TraineeShortInfoResponse traineeShortInfo = new TraineeShortInfoResponse();

        User user = User.builder()
                .userName(userName)
                .firstName(firstName)
                .lastName(lastName)
                .isRemoved(isRemoved)
                .isActive(isActive)
                .build();
        Trainer trainer = Trainer.builder()
                .isRemoved(isRemoved)
                .specialization(trainingType)
                .trainingPartnerships(partnerships)
                .user(user)
                .build();
        user.setTrainerProfile(trainer);

        Trainee removedPartnershipTrainee = Trainee.builder().build();

        User traineeUser = Mockito.mock(User.class);
        Trainee trainee = Trainee.builder().user(traineeUser).build();
        Mockito.when(traineeUser.isActive()).thenReturn(true);

        User inactiveTraineeUser = Mockito.mock(User.class);
        Trainee inactiveTrainee = Trainee.builder().user(inactiveTraineeUser).build();
        Mockito.when(inactiveTraineeUser.isActive()).thenReturn(false);

        partnerships.add(TrainingPartnership.builder().id(1).isRemoved(true).trainer(trainer).trainee(removedPartnershipTrainee).build());
        partnerships.add(TrainingPartnership.builder().id(2).isRemoved(false).trainer(trainer).trainee(inactiveTrainee).build());
        partnerships.add(TrainingPartnership.builder().id(3).isRemoved(false).trainer(trainer).trainee(trainee).build());

        Mockito.when(conversionService.convert(trainee, TraineeShortInfoResponse.class)).thenReturn(traineeShortInfo);

        TrainerFullInfoResponse response = converter.convert(trainer);

        assert response != null;
        assertEquals(type, response.getSpecialization());
        assertEquals(firstName, response.getFirstName());
        assertEquals(isActive, response.isActive());
        assertEquals(lastName, response.getLastName());
        assertEquals(userName, response.getUsername());
        List<TraineeShortInfoResponse> traineesInfoList= response.getTraineesList();
        assertEquals(1, traineesInfoList.size());
        assertEquals(traineeShortInfo, traineesInfoList.get(0));
    }

}