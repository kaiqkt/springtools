package filters;

import dto.Authentication;
import handlers.AuthenticationHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationHandler authenticationHandler;

    @Autowired
    public AuthenticationFilter(AuthenticationHandler authenticationHandler) {
        this.authenticationHandler = authenticationHandler;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        Authentication authentication = Optional.ofNullable(token)
                .map(t -> t.startsWith("Bearer ") ? authenticationHandler.handleJWTToken(t.replace("Bearer ", ""))
                        : authenticationHandler.handleAccessToken(t))
                .orElseGet(authenticationHandler::handlePublic);

        logger.info(authentication);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
                .map(header -> header.startsWith("Bearer ") ? header.substring(7) : header)
                .orElse(null);
    }

}
