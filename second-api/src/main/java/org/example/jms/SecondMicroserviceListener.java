package org.example.jms;

import org.example.to_externalize.jms.SecondMicroserviceJms;
import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.example.service.TrainingSummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Message;

@Component
public class SecondMicroserviceListener {
    private static final String QUEUE_PING = SecondMicroserviceJms.QUEUE_PING;

    private static final String QUEUE_ERROR_PING = SecondMicroserviceJms.QUEUE_ERROR_PING;
    private static final String QUEUE_PUT_TRAINING = SecondMicroserviceJms.QUEUE_PUT_TRAINING;

    private static final Logger LOG = LoggerFactory.getLogger(SecondMicroserviceListener.class);

    @Autowired
    TrainingSummaryService service;

    @JmsListener(destination = QUEUE_PING)
    public void receivePing(@Payload String message, Message rawMsg) {
        System.out.println("ping received: " + message);
    }

    @JmsListener(destination = QUEUE_ERROR_PING)
    public void receiveErrorPing(@Payload String message, Message msg) {
        throw new RuntimeException("error ping received: " + message);
    }


    @JmsListener(destination = QUEUE_PUT_TRAINING)
    public void receiveMessage(@Payload SecondMicroservicePutTrainingRequest request, Message rawMsg) {
        service.putTrainingRequest(request);
        System.out.println("put request received: " + request);
    }
}
