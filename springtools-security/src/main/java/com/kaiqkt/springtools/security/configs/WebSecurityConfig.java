package com.kaiqkt.springtools.security.configs;

import com.kaiqkt.springtools.security.filters.AuthenticationFilter;
import com.kaiqkt.springtools.security.handlers.AccessDeniedHandler;
import com.kaiqkt.springtools.security.handlers.RestAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;

import javax.swing.text.html.HTML;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    private final AuthenticationFilter authenticationFilter;

    @Autowired
    public WebSecurityConfig(AuthenticationFilter authenticationFilter) {
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.securityMatchers(matchers -> matchers.requestMatchers(Companion.PATH_MATCHERS));
        httpSecurity.exceptionHandling(handling -> handling.accessDeniedHandler(new AccessDeniedHandler()).authenticationEntryPoint(new RestAuthenticationEntryPoint()));
        httpSecurity.authorizeHttpRequests(authRequest ->
                authRequest.requestMatchers(Companion.PATH_MATCHERS).permitAll().anyRequest().authenticated()
        );
        httpSecurity.addFilterBefore(authenticationFilter, AuthorizationFilter.class);

        return httpSecurity.build();
    }

    public static class Companion {
        private static final String[] PATH_MATCHERS = {
                "/v2/api-docs",
                "/v3/api-docs/**",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui/index.html",
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/webjars/**",
                "/api-docs.yml",
                "/docs",
                "/healthcheck"
        };
    }
}
