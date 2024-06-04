package org.example.service;

import org.example.TrainingSummaryEntity;
import org.example.repository.TrainingSummaryRepository;
import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryRequest;
import org.example.to_externalize.requests_responses.TrainingDurationSummaryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TrainingSummaryService {

    @Autowired
    TrainingSummaryRepository repository;

    public void putTrainingRequest(SecondMicroservicePutTrainingRequest request) {
        int year =  request.getDate().toLocalDateTime().getYear();
        int month = request.getDate().toLocalDateTime().getMonth().getValue();
        long durationDelta = request.getDuration().toMinutes();
        Timestamp monthDate = Timestamp.valueOf(LocalDate.of(year, month, 1).atStartOfDay());

        TrainingSummaryEntity entity;
        Optional<TrainingSummaryEntity> optionalExistingSummary = repository.findTrainingSummaryEntityByMonthAndUsername(monthDate, request.getUsername());
        if (optionalExistingSummary.isPresent()) {
            entity = optionalExistingSummary.get();
            //duration start
            long oldDuration = entity.getDuration();
            if (request.getAction().equals(SecondMicroservicePutTrainingRequest.ACTION_ADD)) {
                entity.setDuration(oldDuration + durationDelta);
            }
            else if (durationDelta < oldDuration) {
                entity.setDuration(oldDuration - durationDelta);
            }
            else
                entity.setDuration(0);
            //duration done
        } else {
            //new entity
            entity = TrainingSummaryEntity.builder()
                    .username(request.getUsername())
                    .month(monthDate)
                    .duration(0)
                    .build();
            if (request.getAction().equals(SecondMicroservicePutTrainingRequest.ACTION_ADD))
                entity.setDuration(durationDelta);
            entity.setFirstName(request.getFirstName());
            entity.setLastName(request.getLastName());
            entity.setActive(request.isActive());
            //new entity done
        }

        //irrelevant field update done
        //update irrelevant fields of other entities from same username
        List<TrainingSummaryEntity> list = repository.findAllByUsername(request.getUsername());
        List<TrainingSummaryEntity> toSave = new ArrayList<>();
        list.forEach(e -> {
            boolean needSave = !e.getFirstName().equals(request.getFirstName());
            if (!e.getLastName().equals(request.getLastName()))
                needSave = true;
            if (e.isActive() != request.isActive())
                needSave = true;
            if (needSave) {
                e.setFirstName(request.getFirstName());
                e.setLastName(request.getLastName());
                e.setActive(request.isActive());
                toSave.add(e);
            }
        });
        repository.saveAll(toSave);
        repository.save(entity);

    }
    public TrainingDurationSummaryResponse getTrainingSummary(TrainingDurationSummaryRequest request, String username) {
        List<Timestamp> months;
        try {
            months = request.getMonths().stream()
                    .map(p -> Timestamp.valueOf(LocalDate.of(p.getFirst(), p.getSecond(), 1).atStartOfDay()))
                    .collect(Collectors.toList());
        }
        catch (Exception ex) {
            return TrainingDurationSummaryResponse.builder()
                    .executionStatus(TrainingDurationSummaryResponse.STATUS_BAD_REQUEST)
                    .username(username)
                    .name("")
                    .surname("")
                    .duration(Duration.ZERO)
                    .active(false)
                    .build();
        }
        List<TrainingSummaryEntity> foundEntities = new ArrayList<>();
        months.forEach(m -> {
            repository.findTrainingSummaryEntityByMonthAndUsername(m,username).ifPresent(foundEntities::add);
        });
        if (foundEntities.isEmpty()) {
            return TrainingDurationSummaryResponse.builder()
                    .executionStatus(TrainingDurationSummaryResponse.STATUS_NOT_FOUND)
                    .username(username)
                    .name("")
                    .surname("")
                    .duration(Duration.ZERO)
                    .active(false)
                    .build();
        }
        long totalDuration = 0;
        for (TrainingSummaryEntity e: foundEntities) {
            totalDuration += e.getDuration();
        }

        return TrainingDurationSummaryResponse.builder()
                .executionStatus(TrainingDurationSummaryResponse.STATUS_FOUND)
                .username(username)
                .name(foundEntities.get(0).getFirstName())
                .surname(foundEntities.get(0).getLastName())
                .duration(Duration.ofMinutes(totalDuration))
                .active(foundEntities.get(0).isActive())
                .build();
    }
    public long getTrainingSummaryNumber() {
        return repository.count();
    }
}
