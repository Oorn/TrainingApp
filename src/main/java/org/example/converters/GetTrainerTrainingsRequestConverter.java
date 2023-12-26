package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetTrainerTrainingsRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTrainerTrainingsRequestConverter implements Converter<GetTrainerTrainingsRequest, TrainingSearchFilter> {
    @Override
    public TrainingSearchFilter convert(GetTrainerTrainingsRequest source) {
        return TrainingSearchFilter.builder()
                .trainerName(source.getUsername())
                .traineeName(source.getTraineeUsername())
                .dateFrom(source.getDateFrom())
                .dateTo(source.getDateTo())
                .build();
    }
}
