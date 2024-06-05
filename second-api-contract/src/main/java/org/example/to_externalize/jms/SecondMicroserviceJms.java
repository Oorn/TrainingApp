package org.example.to_externalize.jms;

import org.example.to_externalize.requests_responses.SecondMicroservicePutTrainingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.jms.ConnectionFactory;
import java.util.function.Supplier;

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

    @Autowired(required = false)
    @Qualifier("logging-uuid-export")
    private Supplier<String> uuidSupplier;
    @PostConstruct
    private void init(){
        jmsTemplateRegular = new JmsTemplate(jmsConnectionFactory);
        jmsTemplateRegular.setMessageConverter(jmsConverter);

        jmsTemplatePersistent = new JmsTemplate(jmsConnectionFactory);
        jmsTemplatePersistent.setMessageConverter(jmsConverter);
        jmsTemplatePersistent.setDeliveryPersistent(true);
        jmsTemplatePersistent.setSessionTransacted(true);

        if (uuidSupplier == null)
            uuidSupplier = () -> "no-id-supplier-presented";
    }


    public void ping(){
        jmsTemplateRegular.convertAndSend(QUEUE_PING, "jms pong", (m) -> {
            m.setStringProperty("logging-uuid", uuidSupplier.get());
            return m;});
    }

    public void putTraining(SecondMicroservicePutTrainingRequest request) {
        jmsTemplatePersistent.convertAndSend(QUEUE_PUT_TRAINING, request, (m) -> {
            m.setStringProperty("logging-uuid", uuidSupplier.get());
            return m;});
    }

    public void errorPing() {
        jmsTemplatePersistent.convertAndSend(QUEUE_ERROR_PING, "error pong", (m) -> {
            m.setStringProperty("logging-uuid", uuidSupplier.get());
            return m;});
    }

}
