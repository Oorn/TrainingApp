package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Trainer;
import org.example.requests_responses.trainer.TrainerShortInfoResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerShortInfoResponseConverter implements Converter<Trainer, TrainerShortInfoResponse> {
    @Override
    public TrainerShortInfoResponse convert(Trainer trainer) {
        return TrainerShortInfoResponse.builder()
                .firstName(trainer.getUser().getFirstName())
                .lastName(trainer.getUser().getLastName())
                .username(trainer.getUser().getUserName())
                .specialization(trainer.getSpecialization().getTrainingType())
                .build();
    }
}
