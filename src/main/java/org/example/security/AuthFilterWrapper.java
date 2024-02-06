package org.example.security;


import lombok.Getter;
import lombok.Setter;
import org.example.domain_entities.Trainee;
import org.example.domain_entities.Trainer;
import org.example.domain_entities.User;
import org.example.repository.TraineeHibernateRepository;
import org.example.repository.TrainerHibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
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

    @Setter(onMethod_={@Autowired})
    private TraineeHibernateRepository traineeRepository;

    @Setter(onMethod_={@Autowired})
    private TrainerHibernateRepository trainerRepository;

    @Getter
    private final AuthFilter authFilter = new AuthFilter(this);


    private static final String USERNAME = "username";
    public static final String TRAINER_AUTH_PATH_PATTERN = "/trainer/{" + USERNAME + "}/**";
    public static final String TRAINEE_AUTH_PATH_PATTERN = "/trainee/{" + USERNAME + "}/**";

    private final AntPathRequestMatcher trainerMatcher = new AntPathRequestMatcher(TRAINER_AUTH_PATH_PATTERN);
    private final AntPathRequestMatcher traineeMatcher = new AntPathRequestMatcher(TRAINEE_AUTH_PATH_PATTERN);

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

        String pathUsername;
        User authUser = null;
        //trainee
        if (traineeMatcher.matches(httpRequest)) {
            pathUsername = traineeMatcher.matcher(httpRequest).getVariables().get(USERNAME);
            //token doesn't match path, no auth
            if (!pathUsername.equals(tokenUsername)) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            Optional<Trainee> authTrainee = traineeRepository.findTraineeByUsername(tokenUsername);
            //no such user in database, can only happen if user was hard deleted and token has not yet expired
            if (authTrainee.isEmpty()) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            //user has been soft removed, no auth
            if (authTrainee.get().isRemoved()) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            //state - token is present and valid, token matches path and has corresponding user in db, user is not removed
            authUser = authTrainee.get().getUser();
        }
        //trainer
        else if (trainerMatcher.matches(httpRequest)) {
            pathUsername = trainerMatcher.matcher(httpRequest).getVariables().get(USERNAME);
            //token doesn't match path, no auth
            if (!pathUsername.equals(tokenUsername)) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            Optional<Trainer> authTrainer = trainerRepository.findTrainerByUsername(tokenUsername);
            //no such user in database, can only happen if user was hard deleted and token has not yet expired
            if (authTrainer.isEmpty()) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            //user has been soft removed, no auth
            if (authTrainer.get().isRemoved()) {
                filterChain.doFilter(servletRequest, httpServletResponse);
                return;
            }
            //state - token is present and valid, token matches path and has corresponding user in db, user is not removed
            authUser = authTrainer.get().getUser();
        }
        //only explicit trainer/trainee calls are supposed to be authenticated
        if (authUser == null) {
            filterChain.doFilter(servletRequest, httpServletResponse);
            return;
        }

        //finally, grant auth
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(authUser, null, AuthorityUtils.NO_AUTHORITIES);
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //auto return refreshed token
        Optional<String> newToken = tokenUtils.refreshToken(authToken);
        newToken.ifPresent(s -> httpServletResponse.setHeader(JWTPropertiesConfig.AUTH_TOKEN_HEADER, s));

        filterChain.doFilter(servletRequest, httpServletResponse);
    }
}
