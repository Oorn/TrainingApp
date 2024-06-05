package org.example.logging.request_backtrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Component
public class RequestBacktraceProvider {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final ThreadLocal<RequestTrace> record = ThreadLocal.withInitial(
            () -> RequestTrace.builder().Empty(true).uuid("NO-ID").build()
    );
    public void recordCurrentBacktrace(HttpServletRequest request){
        if (!record.get().isEmpty())
            log.warn("attempted backtrace rewrite without removal. Content: " + record.get().toString());
        String uuid = request.getHeader("logging-uuid");
        if (uuid == null)
            uuid = UUID.randomUUID().toString();
        record.set(RequestTrace.builder()
                .Empty(false)
                .uuid(uuid)
                .request(request).build());
    }
    public void recordCurrentJMSMessage(Message msg) {
        if (!record.get().isEmpty())
            log.warn("attempted backtrace rewrite without removal. Content: " + record.get().toString());
        String uuid;
        try {
            //TODO alternative more compact way of assigning default value for null? ask in code review
            //uuid = Optional.ofNullable(msg.getStringProperty("logging-uuid")).orElse(UUID.randomUUID().toString());

            uuid = msg.getStringProperty("logging-uuid");
            if (uuid == null)
                uuid = UUID.randomUUID().toString();

        } catch (JMSException e) {
            log.error("Exception when parsing JMS properties for uuid, falling back on random uuid");
            uuid = UUID.randomUUID().toString();
        }
        if (uuid == null)
            uuid = UUID.randomUUID().toString();
        record.set(RequestTrace.builder()
                .Empty(false)
                .uuid(uuid)
                .request(null).build());
    }
    public void cleanCurrentBacktrace(){
        if (record.get().isEmpty())
            log.warn("attempted removal of empty backtrace");
        record.remove();
    }

    public RequestTrace getCurrentBacktrace(){
        if (record.get().isEmpty())
            log.warn("attempted retrieval of empty backtrace");
        return record.get();
    }

}
