package org.example.security;


import lombok.Getter;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
//silly delegation because Spring auto registers filter beans, which we don't want with Spring Security
public class AuthFilterWrapper {

    public static class AuthFilter implements Filter {
        private final AuthFilterWrapper parent;
        AuthFilter(AuthFilterWrapper parent) {
            this.parent = parent;
        }
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            parent.doFilter(servletRequest, servletResponse, filterChain);
        }
    }
    @Setter(onMethod_={@Autowired})
    private  JWTUtils tokenUtils;


    @Getter
    private final AuthFilter authFilter = new AuthFilter(this);



    private void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String authToken = httpRequest.getHeader(JWTPropertiesConfig.AUTH_TOKEN_HEADER);

        //no token present, skip to next filter
        if (authToken == null) {
            filterChain.doFilter(servletRequest, httpServletResponse);
            return;
        }

        //invalid token, skip to next filter
        if (!tokenUtils.validateToken(authToken)) {
            filterChain.doFilter(servletRequest, httpServletResponse);
            return;
        }

        //token without username or already authenticated. Should never happen
        String tokenUsername = tokenUtils.getUsernameFromToken(authToken);
        if (tokenUsername == null || SecurityContextHolder.getContext().getAuthentication() != null) {
            filterChain.doFilter(servletRequest, httpServletResponse);
            return;
        }

        //finally, grant auth
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(tokenUsername, null, AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //auto return refreshed token
        Optional<String> newToken = tokenUtils.refreshToken(authToken);
        newToken.ifPresent(s -> httpServletResponse.setHeader(JWTPropertiesConfig.AUTH_TOKEN_HEADER, s));

        filterChain.doFilter(servletRequest, httpServletResponse);
    }
}
