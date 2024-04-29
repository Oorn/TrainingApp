package org.example.to_externalize.jms;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class SecondMicroserviceJmsService {
    private static final String QUEUE_NAME_BASE = "second_micro.queue";
    private static final String QUEUE_PING = QUEUE_NAME_BASE + ".ping";
    private static final String QUEUE_PUT_TRAINING = QUEUE_NAME_BASE + ".put_training";


    @Autowired
    private JmsTemplate jmsTemplate;

    public void ping(){
        jmsTemplate.convertAndSend(QUEUE_PING, "jms ping");
    }

    public void putTraining(SecondMicroservicePutTrainingRequest request) {
        jmsTemplate.convertAndSend(QUEUE_PUT_TRAINING, request);
    }

}
