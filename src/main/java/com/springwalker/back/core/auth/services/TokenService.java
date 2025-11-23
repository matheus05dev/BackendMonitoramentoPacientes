package com.springwalker.back.core.auth.services;

import com.springwalker.back.user.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private static final String ISSUER = "API InfraMed";
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("-03:00");

    @Value("${api.security.token.secret:secret}")
    private String secret;

    public String gerarToken(User user) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withClaim("role", user.getRole().name()) // Adicionar role para validações futuras
                    .withExpiresAt(dataExpiracaoAccessToken())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar access token jwt", exception);
        }
    }

    public String gerarRefreshToken(User user) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer(ISSUER)
                    .withSubject(user.getUsername())
                    .withExpiresAt(dataExpiracaoRefreshToken())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("Erro ao gerar refresh token jwt", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(secret);
            return JWT.require(algoritmo)
                    .withIssuer(ISSUER)
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }

    private Instant dataExpiracaoAccessToken() {
        // Access token válido por 15 minutos
        return LocalDateTime.now().plusMinutes(15).toInstant(ZONE_OFFSET);
    }

    private Instant dataExpiracaoRefreshToken() {
        // Refresh token válido por 7 dias
        return LocalDateTime.now().plusDays(7).toInstant(ZONE_OFFSET);
    }
}
