package org.example.jms;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.service.TrainingSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class SecondMicroserviceListener {
    private static final String QUEUE_NAME_BASE = "second_micro.queue";
    private static final String QUEUE_PING = QUEUE_NAME_BASE + ".ping";
    private static final String QUEUE_PUT_TRAINING = QUEUE_NAME_BASE + ".put_training";

    private static final Logger LOG = LoggerFactory.getLogger(SecondMicroserviceListener.class);

    @Autowired
    TrainingSummaryService service;

    @JmsListener(destination = QUEUE_PING)
    public void receivePing(String message) {
        System.out.println("ping received: " + message);
    }

    @JmsListener(destination = QUEUE_PUT_TRAINING)
    public void receiveMessage(SecondMicroservicePutTrainingRequest request) {
        service.putTrainingRequest(request);
        System.out.println("put request received: " + request);
    }
}
