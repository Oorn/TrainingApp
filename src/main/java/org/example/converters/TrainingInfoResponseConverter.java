package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Training;
import org.example.requests_responses.training.TrainingInfoResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class TrainingInfoResponseConverter implements Converter<Training, TrainingInfoResponse> {
    @Override
    public TrainingInfoResponse convert(Training source) {
        return TrainingInfoResponse.builder()
                .traineeUsername(source.getTrainingPartnership().getTrainee().getUser().getUserName())
                .trainerUsername(source.getTrainingPartnership().getTrainer().getUser().getUserName())
                .name(source.getTrainingName())
                .type(source.getTrainingPartnership().getTrainer().getSpecialization().getTrainingType())
                .date(source.getTrainingDateFrom())
                .duration(Duration.between(source.getTrainingDateFrom(), source.getTrainingDateTo()))
                .build();
    }
}
