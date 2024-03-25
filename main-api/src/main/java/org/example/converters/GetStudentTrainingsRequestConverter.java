package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetStudentTrainingsRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetStudentTrainingsRequestConverter implements Converter<GetStudentTrainingsRequest, TrainingSearchFilter> {
    @Override
    public TrainingSearchFilter convert(GetStudentTrainingsRequest source) {
        return TrainingSearchFilter.builder()
                .mentorName(source.getMentorUsername())
                .specialisation(source.getType())
                .dateFrom(source.getDateFrom())
                .dateTo(source.getDateTo())
                .build();
    }
}
