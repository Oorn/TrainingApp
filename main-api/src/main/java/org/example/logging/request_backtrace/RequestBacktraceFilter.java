package org.example.logging.request_backtrace;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class RequestBacktraceFilter extends OncePerRequestFilter {

    @Autowired
    private RequestBacktraceProvider backtraceService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        backtraceService.recordCurrentBacktrace(request);
        try {
            filterChain.doFilter(request, response);
        }
        finally {
            backtraceService.cleanCurrentBacktrace();
        }
    }
}
