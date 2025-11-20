package com.springwalker.back.core.auth.services;

import com.springwalker.back.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secret", "secret");
    }

    @Test
    @DisplayName("Gerar token com sucesso")
    void gerarToken() {
        User user = new User();
        user.setUsername("test.user");

        String token = tokenService.gerarToken(user);

        assertNotNull(token);
    }

    @Test
    @DisplayName("Obter sujeito do token com sucesso")
    void getSubject() {
        User user = new User();
        user.setUsername("test.user");

        String token = tokenService.gerarToken(user);
        String subject = tokenService.getSubject(token);

        assertEquals("test.user", subject);
    }

    @Test
    @DisplayName("Lançar exceção para token inválido")
    void getSubjectInvalidToken() {
        assertThrows(RuntimeException.class, () -> tokenService.getSubject("invalid.token"));
    }
}
