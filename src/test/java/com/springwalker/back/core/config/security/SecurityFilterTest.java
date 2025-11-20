package com.springwalker.back.core.config.security;

import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import com.springwalker.back.user.role.Role;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SecurityFilter securityFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Filtrar requisição com token válido")
    void doFilterInternalWithValidToken() throws ServletException, IOException {
        String token = "valid.token";
        String username = "test.user";
        User user = new User();
        user.setUsername(username);
        user.setRole(Role.ADMIN); // <-- CORREÇÃO AQUI

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertEquals(username, ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Filtrar requisição sem token")
    void doFilterInternalWithoutToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Filtrar requisição com token inválido")
    void doFilterInternalWithInvalidToken() throws ServletException, IOException {
        String token = "invalid.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenThrow(new RuntimeException("Token JWT inválido ou expirado!"));

        // A verificação do token acontece antes do doFilter, então a exceção deve ser lançada
        // e o filtro não deve continuar.
        assertThrows(RuntimeException.class, () -> {
            securityFilter.doFilterInternal(request, response, filterChain);
        });

        verify(filterChain, never()).doFilter(request, response);
    }

    @Test
    @DisplayName("Filtrar requisição com usuário não encontrado")
    void doFilterInternalWithUserNotFound() throws ServletException, IOException {
        String token = "valid.token";
        String username = "non.existent.user";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(tokenService.getSubject(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // A busca do usuário acontece antes do doFilter, então a exceção deve ser lançada
        // e o filtro não deve continuar.
        assertThrows(RuntimeException.class, () -> {
            securityFilter.doFilterInternal(request, response, filterChain);
        });

        verify(filterChain, never()).doFilter(request, response);
    }
}
