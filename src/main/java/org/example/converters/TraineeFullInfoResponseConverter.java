package org.example.converters;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.requests_responses.trainee.TraineeFullInfoResponse;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TraineeFullInfoResponseConverter implements Converter<Trainee, TraineeFullInfoResponse> {

    @Setter(onMethod_={@Autowired})
    private ConversionService converter;

    @Override
    public TraineeFullInfoResponse convert(Trainee trainee) {
        return TraineeFullInfoResponse.builder()
                .address(trainee.getAddress())
                .dateOfBirth(trainee.getDateOfBirth())
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUserName())
                .trainersList(trainee.getTrainingPartnerships().stream()
                        .filter(t -> !t.isRemoved())
                        .filter(t -> t.getTrainer().getUser().isActive())
                        .map(p->converter.convert(p.getTrainer(), TrainerShortInfoResponse.class))
                        .collect(Collectors.toList()))
                .build();
    }
}
