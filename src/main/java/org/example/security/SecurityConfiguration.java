package org.example.security;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Setter(onMethod_={@Autowired})
    private AuthFilterWrapper authFilterWrapper;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(
                        (request, response, ex) -> response.sendError(
                                HttpServletResponse.SC_UNAUTHORIZED,
                                ex.getMessage()
                        )
                )
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                //ant matchers
                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**","/configuration/ui/**", "/configuration/security/**", "/webjars/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/ping/**", "/specialisations", "/mentor", "/student", "/student/{username}/login", "/mentor/{username}/login" ).permitAll()
                .antMatchers("/mentor/**", "/student/**").authenticated()
                //.antMatchers("/actuator/**").access("hasIpAddress('127.0.0.1') or hasIpAddress('::1')")
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .denyAll();


        http.addFilterBefore(authFilterWrapper.getAuthFilter(), BasicAuthenticationFilter.class);
        return http.build();

    }

}
