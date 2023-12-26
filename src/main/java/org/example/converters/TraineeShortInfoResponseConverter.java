package org.example.converters;

import lombok.RequiredArgsConstructor;
import org.example.domain_entities.Trainee;
import org.example.requests_responses.trainee.TraineeShortInfoResponse;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeShortInfoResponseConverter implements Converter<Trainee, TraineeShortInfoResponse> {
    @Override
    public TraineeShortInfoResponse convert(Trainee trainee) {
        return TraineeShortInfoResponse.builder()
                .firstName(trainee.getUser().getFirstName())
                .lastName(trainee.getUser().getLastName())
                .username(trainee.getUser().getUserName())
                .build();
    }
}
