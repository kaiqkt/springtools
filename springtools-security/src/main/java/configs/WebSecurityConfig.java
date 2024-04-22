package configs;

import filters.AuthenticationFilter;
import handlers.AuthenticationEntryPointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.ArrayList;

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Value("#{${springtools.ignore-paths}}")
    private ArrayList<String> paths;

    private final AuthenticationFilter authenticationFilter;

    public WebSecurityConfig(OncePerRequestFilter authenticationFilter) {
        this.authenticationFilter = (AuthenticationFilter) authenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(AbstractHttpConfigurer::disable);
        httpSecurity.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        httpSecurity.exceptionHandling(handler -> handler.authenticationEntryPoint(new AuthenticationEntryPointImpl()));
        httpSecurity.authorizeHttpRequests(authRequest ->
                authRequest.requestMatchers((RequestMatcher) paths).permitAll().anyRequest().authenticated()
        );
        httpSecurity.addFilterBefore(authenticationFilter, AuthorizationFilter.class);

        return httpSecurity.build();
    }
}
