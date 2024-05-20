package com.rxvlvxr.bank.app.impl.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rxvlvxr.bank.domain.user.UserPrinciple;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
public class JwtProvider {
    @Value("${com.rxvlvxr.bank.jwtSecret}")
    private String secret;
    @Value("${com.rxvlvxr.bank.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {
        UserPrinciple userPrinciple = (UserPrinciple) authentication.getPrincipal();
        Date expirationDate = Date.from(ZonedDateTime.now().plusMinutes(jwtExpiration).toInstant());

        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", userPrinciple.getUsername())
                .withClaim("currentUser", toJsonString(userPrinciple))
                .withIssuedAt(new Date())
                .withIssuer("bank-api")
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(secret));
    }


    public String validateTokenAndRetrieveClaim(String token) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret)).withSubject("User Details").withIssuer("bank-api").build();

        DecodedJWT jwt = verifier.verify(token);
        return jwt.getClaim("username").asString();
    }

    private String toJsonString(Serializable object) {
        ObjectMapper om = new ObjectMapper();
        om.registerModule(new JavaTimeModule());
        ObjectWriter writer = om.writer().withDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(String.format("Could not transform object '%s' to JSON: ", object), e);
        }
    }
}
