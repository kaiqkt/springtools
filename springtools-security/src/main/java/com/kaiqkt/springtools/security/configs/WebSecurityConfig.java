package com.kaiqkt.springtools.security.configs;

import com.kaiqkt.springtools.security.filters.AuthenticationFilter;
import com.kaiqkt.springtools.security.handlers.AuthenticationEntryPointImpl;
import com.kaiqkt.springtools.security.utils.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.ArrayList;
import java.util.List;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AuthenticationProperties.class)
public class WebSecurityConfig {

    private final AuthenticationFilter authenticationFilter;
    @Qualifier("restAuthenticationEntryPoint")
    private final AuthenticationEntryPoint entryPoint;

    @Autowired
    public WebSecurityConfig(AuthenticationFilter authenticationFilter, AuthenticationEntryPoint entryPoint) {
        this.authenticationFilter =  authenticationFilter;
        this.entryPoint = entryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.exceptionHandling(handler -> handler.authenticationEntryPoint(entryPoint));
        httpSecurity.authorizeHttpRequests(authRequest -> {
            for (String path : Companion.PATH_MATCHERS) {
                authRequest.requestMatchers(path).permitAll();
            }
            authRequest.anyRequest().authenticated();
        });
        httpSecurity.addFilterBefore(authenticationFilter, AuthorizationFilter.class);

        return httpSecurity.build();
    }

    public static class Companion {
        private static final List<String> PATH_MATCHERS = new ArrayList<>();

        static {
            PATH_MATCHERS.add("/v2/api-docs");
            PATH_MATCHERS.add("/v3/api-docs/**");
            PATH_MATCHERS.add("/configuration/ui");
            PATH_MATCHERS.add("/swagger-resources/**");
            PATH_MATCHERS.add("/configuration/security");
            PATH_MATCHERS.add("/swagger-ui/index.html");
            PATH_MATCHERS.add("/swagger-ui.html");
            PATH_MATCHERS.add("/swagger-ui/**");
            PATH_MATCHERS.add("/webjars/**");
            PATH_MATCHERS.add("/api-docs.yml");
            PATH_MATCHERS.add("/docs");
            PATH_MATCHERS.add("/health");
        }

    }
}
