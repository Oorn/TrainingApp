package org.example.to_externalize.jms;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;

@Service
public class SecondMicroserviceJms {
    public static final String QUEUE_NAME_BASE = "second_micro.queue";
    public static final String QUEUE_PING = QUEUE_NAME_BASE + ".ping";

    public static final String QUEUE_ERROR_PING = QUEUE_NAME_BASE + ".error_ping";
    public static final String QUEUE_PUT_TRAINING = QUEUE_NAME_BASE + ".put_training";


    @Autowired
    private MessageConverter jmsConverter;
    @Autowired
    private ConnectionFactory jmsConnectionFactory;
    private JmsTemplate jmsTemplateRegular;
    private JmsTemplate jmsTemplatePersistent;
    @PostConstruct
    private void init(){
        jmsTemplateRegular = new JmsTemplate(jmsConnectionFactory);
        jmsTemplateRegular.setMessageConverter(jmsConverter);

        jmsTemplatePersistent = new JmsTemplate(jmsConnectionFactory);
        jmsTemplatePersistent.setMessageConverter(jmsConverter);
        jmsTemplatePersistent.setDeliveryPersistent(true);
        jmsTemplatePersistent.setSessionTransacted(true);
    }


    public void ping(){
        jmsTemplateRegular.convertAndSend(QUEUE_PING, "jms pong");
    }

    public void putTraining(SecondMicroservicePutTrainingRequest request) {
        jmsTemplatePersistent.convertAndSend(QUEUE_PUT_TRAINING, request);
    }

    public void errorPing() {
        jmsTemplatePersistent.convertAndSend(QUEUE_ERROR_PING, "error pong");
    }

}
