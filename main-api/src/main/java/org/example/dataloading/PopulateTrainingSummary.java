package org.example.dataloading;

import org.example.domain_entities.Training;
import org.example.repository.TrainingHibernateRepository;
import org.example.to_externalize.SecondMicroserviceWrapper;
import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PopulateTrainingSummary {
    @Autowired
    SecondMicroserviceWrapper secondMicroservice;

    @Autowired
    TrainingHibernateRepository trainingHibernateRepository;

    @PostConstruct
    void populateTrainingSummaries() {
        Runnable runnable = () -> {
            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long count = secondMicroservice.getTrainingSummaryCount();
            if (count != 0)
                return;
            List<Training> list = trainingHibernateRepository.findAll().stream()
                    .filter(t -> !t.isRemoved())
                    .collect(Collectors.toList());
            list.forEach((t) -> {
                        secondMicroservice.putTraining(
                                SecondMicroservicePutTrainingRequest.builder()
                                        .action(SecondMicroservicePutTrainingRequest.ACTION_ADD)
                                        .duration(Duration.between(t.getTrainingDateFrom().toLocalDateTime(), t.getTrainingDateTo().toLocalDateTime()))
                                        .date(t.getTrainingDateFrom())
                                        .firstName(t.getPartnership().getMentor().getUser().getFirstName())
                                        .isActive(t.getPartnership().getMentor().getUser().isActive())
                                        .firstName(t.getPartnership().getMentor().getUser().getFirstName())
                                        .lastName(t.getPartnership().getMentor().getUser().getLastName())
                                        .build()
                        );
                    });
        };
        Thread delayedThread = new Thread(runnable);
        delayedThread.start();
    }
}
