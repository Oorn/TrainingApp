package org.example.security;

import lombok.Setter;
import org.example.security.bruteforce.BruteForceProtectionService;
import org.example.service.CredentialsService;
import org.example.service.MentorService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration{

    @Setter(onMethod_={@Autowired})
    private AuthFilterWrapper authFilterWrapper;

    @Setter(onMethod_={@Autowired})
    private CredentialsService credentialsService;

    @Setter(onMethod_={@Autowired})
    private StudentService studentService;

    @Setter(onMethod_={@Autowired})
    private MentorService mentorService;

    @Setter(onMethod_={@Autowired})
    private BruteForceProtectionService protectionService;

    @Order(Ordered.HIGHEST_PRECEDENCE + 2)
    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .cors()
                .and()
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
                .antMatchers("/ping/**", "/specialisations", "/mentor", "/student", "/student/{username}/login", "/mentor/{username}/login").permitAll()
                .antMatchers("/mentor/**", "/student/**").authenticated()
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .denyAll();

        http.addFilterBefore(authFilterWrapper.getAuthFilter(), BasicAuthenticationFilter.class);
        return http.build();

    }

    @Order(Ordered.HIGHEST_PRECEDENCE + 1)
    @Bean
    public SecurityFilterChain loginFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/UI-login/**")
                .csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                //ant matchers
                .antMatchers("/UI-login*", "/UI-login/login*" ).permitAll()
                .antMatchers("/UI-login/content*").authenticated()
                .anyRequest()
                .denyAll()
                .and()
                .formLogin()
                .loginPage("/UI-login/login")
                .loginProcessingUrl("/UI-login/login")
                .defaultSuccessUrl("/UI-login")
                .permitAll()
                .and()
                .logout()
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/UI-login")
                .logoutUrl("/UI-login/logout")
                .permitAll()
                .and()
                .authenticationManager(getUIAuthManager());

        return http.build();

    }


    private AuthenticationManager getUIAuthManager() {
        return authentication -> {
            String name = authentication.getName();
            String password = authentication.getCredentials().toString();
            if (!protectionService.verifyLoginAccess(name)) {
                protectionService.registerLoginFailure(name);
                throw new BadCredentialsException("user has been temporarily blocked");
            }
            try {
                if (credentialsService.validateUsernamePassword(name, password)) {
                    List<GrantedAuthority> grantedAuths = new ArrayList<>();
                    try {
                        if (studentService.isStudent(name))
                            grantedAuths.add(new SimpleGrantedAuthority("Student"));
                    }
                    catch (Exception ignored){}
                    try {
                        if (mentorService.isMentor(name))
                            grantedAuths.add(new SimpleGrantedAuthority("Mentor"));
                    }
                    catch (Exception ignored){}
                    final UserDetails principal = new User(name, password, grantedAuths);
                    return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
                }
            }
            catch (Exception ignored){}
            protectionService.registerLoginFailure(name);
            throw new BadCredentialsException("password doesn't match");
        };
    }

}
