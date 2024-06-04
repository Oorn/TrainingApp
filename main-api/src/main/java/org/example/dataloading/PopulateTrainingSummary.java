package org.example.dataloading;

import org.example.repository.TrainingHibernateRepository;
import org.example.to_externalize.SecondMicroserviceWrapper;
import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Duration;

@Component
public class PopulateTrainingSummary {
    @Autowired
    SecondMicroserviceWrapper secondMicroservice;

    @Autowired
    TrainingHibernateRepository trainingHibernateRepository;

    @PostConstruct
    void populateTrainingSummaries() {
        long count = secondMicroservice.getTrainingSummaryCount();
        if (count == 0)
            trainingHibernateRepository.findAll().stream()
                .filter(t->!t.isRemoved())
                .forEach((t) -> {
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
    }
}
