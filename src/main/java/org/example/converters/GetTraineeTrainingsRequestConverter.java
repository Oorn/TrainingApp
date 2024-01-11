package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetTraineeTrainingsRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTraineeTrainingsRequestConverter implements Converter<GetTraineeTrainingsRequest, TrainingSearchFilter> {
    @Override
    public TrainingSearchFilter convert(GetTraineeTrainingsRequest source) {
        return TrainingSearchFilter.builder()
                .trainerName(source.getTrainerUsername())
                .trainingType(source.getType())
                .dateFrom(source.getDateFrom())
                .dateTo(source.getDateTo())
                .build();
    }
}
