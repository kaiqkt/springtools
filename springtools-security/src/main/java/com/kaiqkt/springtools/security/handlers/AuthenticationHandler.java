package com.kaiqkt.springtools.security.handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kaiqkt.springtools.security.dto.Authentication;
import com.kaiqkt.springtools.security.enums.ErrorType;
import com.kaiqkt.springtools.security.exceptions.UnauthorizedException;
import com.kaiqkt.springtools.security.utils.AuthenticationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.kaiqkt.springtools.security.enums.Roles.ROLE_API;
import static com.kaiqkt.springtools.security.enums.Roles.ROLE_PUBLIC;

@Component
@EnableConfigurationProperties(AuthenticationProperties.class)
public class AuthenticationHandler {

    private final AuthenticationProperties properties;

    @Autowired
    public AuthenticationHandler(AuthenticationProperties properties) {
        this.properties = properties;
    }

    public Authentication handleJWTToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(properties.getJwtSecret());
        JWTVerifier verifier = JWT.require(algorithm).build();

        try {
            DecodedJWT decodedToken = verifier.verify(token);
            Map<String, Object> data = new HashMap<>(decodedToken.getClaims());

            return new Authentication(data, decodedToken.getToken());
        } catch (TokenExpiredException ex) {
            throw new UnauthorizedException(ex.getMessage(), ErrorType.JWT_TOKEN_EXPIRED);
        } catch (JWTVerificationException ex) {
            throw new UnauthorizedException(ex.getMessage(), ErrorType.INVALID_TOKEN);
        }
    }

    public Authentication handleAccessToken(String token) {
        if (token.equals(properties.getAccessToken())) {
            Map<String, Object> data = new HashMap<>();
            data.put("role", ROLE_API.name());

            return new Authentication(data);
        }

        throw new UnauthorizedException("Invalid Access Token", ErrorType.INVALID_TOKEN);
    }

    public Authentication handlePublic(){
        Map<String, Object> data = new HashMap<>();
        data.put("role", ROLE_PUBLIC.name());

        return new Authentication(data);
    }
}
