package com.kaiqkt.springtools_security.handlers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.kaiqkt.springtools_security.dto.Authentication;
import com.kaiqkt.springtools_security.enums.ErrorType;
import com.kaiqkt.springtools_security.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.kaiqkt.springtools_security.enums.Roles.ROLE_API;
import static com.kaiqkt.springtools_security.enums.Roles.ROLE_PUBLIC;

@Component
public class AuthenticationHandler {

    @Value("#{${springtools.jwt-secret}}")
    private String jwtSecret;

    @Value("#{${springtools.access-secret}}")
    private String accessSecret;

    public AuthenticationHandler(String jwtSecret, String accessSecret) {
        this.jwtSecret = jwtSecret;
        this.accessSecret = accessSecret;
    }

    public Authentication handleJWTToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(jwtSecret);
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
        if (token.equals(accessSecret)) {
            Map<String, Object> data = new HashMap<>();
            data.put("role", ROLE_API.name());

            return new Authentication(data, null);
        }

        throw new UnauthorizedException("Invalid Access Token", ErrorType.INVALID_TOKEN);
    }

    public Authentication handlePublic(){
        Map<String, Object> data = new HashMap<>();
        data.put("role", ROLE_PUBLIC.name());

        return new Authentication(data, null);
    }
}