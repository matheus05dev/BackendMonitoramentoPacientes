package com.springwalker.back.core.auth.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private static final String SECRET = "secret";
    private static final String ISSUER = "API InfraMed";
    private static final ZoneOffset ZONE_OFFSET = ZoneOffset.of("-03:00");

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", SECRET);
    }

    @Test
    @DisplayName("Gerar token de acesso com sucesso")
    void gerarAccessToken() {
        User user = new User("test.user", "password", Role.MEDICO);

        String token = tokenService.gerarToken(user);
        assertNotNull(token);

        String subject = JWT.decode(token).getSubject();
        assertEquals("test.user", subject);
    }

    @Test
    @DisplayName("Gerar refresh token com sucesso")
    void gerarRefreshToken() {
        User user = new User("test.user", "password", Role.MEDICO);

        String token = tokenService.gerarRefreshToken(user);
        assertNotNull(token);

        String subject = JWT.decode(token).getSubject();
        assertEquals("test.user", subject);
    }

    @Test
    @DisplayName("Obter sujeito do token com sucesso")
    void getSubject() {
        User user = new User("test.user", "password", Role.MEDICO);

        String token = tokenService.gerarToken(user);
        String subject = tokenService.getSubject(token);

        assertEquals("test.user", subject);
    }

    @Test
    @DisplayName("Lançar exceção para token inválido")
    void getSubjectInvalidToken() {
        assertThrows(RuntimeException.class, () -> tokenService.getSubject("invalid.token"));
    }

    @Test
    @DisplayName("Lançar exceção para token expirado")
    void getSubjectExpiredToken() {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        String expiredToken = JWT.create()
                .withIssuer(ISSUER)
                .withSubject("test.user")
                .withExpiresAt(Date.from(LocalDateTime.now().minusHours(1).toInstant(ZONE_OFFSET)))
                .sign(algorithm);

        assertThrows(RuntimeException.class, () -> tokenService.getSubject(expiredToken));
    }
}
