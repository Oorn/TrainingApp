package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.repository.dto.TrainingSearchFilter;
import org.example.requests_responses.training.GetMentorTrainingsRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetMentorTrainingsRequestConverter implements Converter<GetMentorTrainingsRequest, TrainingSearchFilter> {
    @Override
    public TrainingSearchFilter convert(GetMentorTrainingsRequest source) {
        return TrainingSearchFilter.builder()
                .studentName(source.getStudentUsername())
                .dateFrom(source.getDateFrom())
                .dateTo(source.getDateTo())
                .build();
    }
}
