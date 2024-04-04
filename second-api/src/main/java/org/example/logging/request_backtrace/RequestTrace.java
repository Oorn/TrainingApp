package org.example.logging.request_backtrace;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import javax.servlet.ServletRequest;

@Builder
@ToString
@Data
public class RequestTrace {

    private String uuid;
    private ServletRequest request;
    private boolean Empty;

}
