package org.example.logging.request_backtrace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

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
        record.set(RequestTrace.builder()
                .Empty(false)
                .uuid(UUID.randomUUID().toString())
                .request(request).build());
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
