package org.example.logging.utility;

import org.example.logging.request_backtrace.RequestBacktraceProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class UuidExporter {

    @Bean("logging-uuid-export")
    Supplier<String> uuidExporterCreation (@Autowired RequestBacktraceProvider provider) {
        return () -> provider.getCurrentBacktrace().getUuid();
    }
}
