package org.example.service.impl;

import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.TrainingPartnership;
import org.example.repository.TraineeRepository;
import org.example.repository.TrainerRepository;
import org.example.repository.TrainingPartnershipRepository;
import org.example.repository.impl.v2.hibernate.TraineeHibernateRepository;
import org.example.repository.impl.v2.hibernate.TrainerHibernateRepository;
import org.example.repository.impl.v2.hibernate.TrainingPartnershipHibernateRepository;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.example.requests_responses.trainingpartnership.AvailableTrainersResponse;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListRequest;
import org.example.requests_responses.trainingpartnership.UpdateTrainingPartnershipListResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.core.convert.ConversionService;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TrainingPartnershipServiceImplTest {

    @Mock
    private TrainerHibernateRepository trainerRepository;

    @Mock
    private TraineeHibernateRepository traineeRepository;

    @Mock
    private TrainingPartnershipHibernateRepository trainingPartnershipRepository;

    @Mock
    private ConversionService converter;

    @InjectMocks
    private TrainingPartnershipServiceImpl service;

    @Test
    void getNotAssignedTrainers() {
        String traineeName = "trainee";
        Trainee trainee = mock(Trainee.class);

        Function<TrainerShortInfoResponse, Trainer> trainerMockFactory = (res) -> {
            Trainer trainer = mock(Trainer.class, Answers.RETURNS_DEEP_STUBS);
            when(converter.convert(trainer, TrainerShortInfoResponse.class)).thenReturn(res);
            when(trainer.isRemoved()).thenReturn(false);
            when(trainer.getUser().isActive()).thenReturn(true);
            return trainer;
        };

        TrainerShortInfoResponse trPresent1res = new TrainerShortInfoResponse()
                , trPresent2res = new TrainerShortInfoResponse()
                , trAbsent1res = new TrainerShortInfoResponse()
                , trAbsent2res = new TrainerShortInfoResponse();

        Trainer trPresent1 = trainerMockFactory.apply(trPresent1res)
                , trPresent2 = trainerMockFactory.apply(trPresent2res)
                , trAbsent1 = trainerMockFactory.apply(trAbsent1res)
                , trAbsent2 = trainerMockFactory.apply(trAbsent2res);

        TrainingPartnership trPart1 = TrainingPartnership.builder()
                .isRemoved(false)
                .trainee(trainee)
                .trainer(trPresent1)
                .build();
        TrainingPartnership trPart2 = TrainingPartnership.builder()
                .isRemoved(false)
                .trainee(trainee)
                .trainer(trPresent2)
                .build();

        when(traineeRepository.findTraineeByUsername(traineeName)).thenReturn(Optional.of(trainee));
        when(trainingPartnershipRepository.findPartnershipByTraineeUsername(traineeName)).thenReturn(List.of(trPart1, trPart2));
        when(trainerRepository.findAll()).thenReturn(List.of(trPresent1, trPresent2, trAbsent1, trAbsent2));

        AvailableTrainersResponse response = service.getNotAssignedTrainers(traineeName);

        verify(traineeRepository, times(1)).findTraineeByUsername(traineeName);
        verify(trainingPartnershipRepository, times(1)).findPartnershipByTraineeUsername(traineeName);
        verify(trainerRepository, times(1)).findAll();
        assert response.getTrainers().contains(trAbsent1res);
        assert response.getTrainers().contains(trAbsent2res);
        assertEquals(2, response.getTrainers().size());
    }

    @Test
    void updateTraineeTrainerList() {

        String traineeName = "trainee";
        Trainee trainee = mock(Trainee.class, Answers.RETURNS_DEEP_STUBS);
        when(trainee.getUser().isActive()).thenReturn(true);

        BiFunction<String, TrainerShortInfoResponse, Trainer> trainerMockFactory = (name, res) -> {
            Trainer trainer = mock(Trainer.class, Answers.RETURNS_DEEP_STUBS);
            when(trainerRepository.findTrainerByUsername(name)).thenReturn(Optional.of(trainer));
            when(converter.convert(trainer, TrainerShortInfoResponse.class)).thenReturn(res);
            when(trainer.isRemoved()).thenReturn(false);
            when(trainer.getUser().isActive()).thenReturn(true);
            when(trainer.getUser().getUserName()).thenReturn(name);
            return trainer;
        };
        BiFunction<Trainee, Trainer, TrainingPartnership> partnershipFactory = (traineeParam, trainerParam) -> TrainingPartnership.builder()
                .trainer(trainerParam)
                .trainee(traineeParam)
                .isRemoved(false)
                .build();

        TrainerShortInfoResponse trPresentPresentRes = new TrainerShortInfoResponse()
                , trPresentAbsentRes = new TrainerShortInfoResponse()
                , trAbsentPresentRes = new TrainerShortInfoResponse();
        final String trPresentPresentName = "trainer1", trPresentAbsentName = "trainer2", trAbsentPresentName = "trainer3";
        Trainer trPresentPresent = trainerMockFactory.apply(trPresentPresentName, trPresentPresentRes)
                , trPresentAbsent = trainerMockFactory.apply(trPresentAbsentName, trPresentAbsentRes)
                , trAbsentPresent = trainerMockFactory.apply(trAbsentPresentName, trAbsentPresentRes);
        TrainingPartnership trPartPresentPresent = partnershipFactory.apply(trainee, trPresentPresent)
                , trPartPresentAbsent = partnershipFactory.apply(trainee, trPresentAbsent);

        when(traineeRepository.findTraineeByUsername(traineeName)).thenReturn(Optional.of(trainee));
        when(trainingPartnershipRepository.findPartnershipByTraineeUsername(traineeName)).thenReturn(List.of(trPartPresentPresent, trPartPresentAbsent));

        UpdateTrainingPartnershipListRequest request = new UpdateTrainingPartnershipListRequest();
        request.setTrainerUsernames(List.of(trPresentPresentName, trAbsentPresentName));
        UpdateTrainingPartnershipListResponse response = service.updateTraineeTrainerList(traineeName, request);

        verify(traineeRepository, times(1)).findTraineeByUsername(traineeName);
        verify(trainingPartnershipRepository, times(2)).findPartnershipByTraineeUsername(traineeName);
        verify(trainingPartnershipRepository, times(2)).saveAndFlush(argThat((trp) -> {
            switch (trp.getTrainer().getUser().getUserName()) {
                case trPresentPresentName:
                case trAbsentPresentName:
                    assertFalse(trp.isRemoved());
                    break;
                case trPresentAbsentName:
                    assertTrue(trp.isRemoved());
                    break;
                default:
                    return false;
            }
            return true;
        }));
    }
}