package org.example.converters;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain_entities.Trainer;
import org.example.requests_responses.trainee.TraineeShortInfoResponse;
import org.example.requests_responses.trainer.TrainerFullInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TrainerFullInfoResponseConverter implements Converter<Trainer, TrainerFullInfoResponse> {

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;

    @Override
    public TrainerFullInfoResponse convert(Trainer trainer) {
        return TrainerFullInfoResponse.builder()
                .specialization(trainer.getSpecialization().getTrainingType())
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUserName())
                .traineesList(trainer.getTrainingPartnerships().stream()
                        .filter(t -> !t.isRemoved())
                        .filter(t -> t.getTrainee().getUser().isActive())
                        .map(p->converter.convert(p.getTrainee(), TraineeShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}

