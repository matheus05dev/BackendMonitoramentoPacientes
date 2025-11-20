package com.springwalker.back.core.config.security;

import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WebSocketAuthInterceptorTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Mock
    private MessageChannel channel;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Interceptar conexão com token válido")
    void preSendWithValidToken() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        String token = "valid.token";
        String username = "test.user";
        User user = new User();
        user.setUsername(username);
            user.setRole(Role.ADMIN);

        accessor.setNativeHeader("Authorization", "Bearer " + token);
        accessor.setLeaveMutable(true); // <-- CORREÇÃO AQUI

        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        when(tokenService.getSubject(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        webSocketAuthInterceptor.preSend(message, channel);

        assertNotNull(accessor.getUser());
        assertEquals(user.getUsername(), ((User) ((Authentication) accessor.getUser()).getPrincipal()).getUsername());
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Interceptar conexão sem header de autorização")
    void preSendWithoutAuthHeader() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        accessor.setLeaveMutable(true);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        webSocketAuthInterceptor.preSend(message, channel);

        assertNull(accessor.getUser());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Interceptar conexão com token inválido")
    void preSendWithInvalidToken() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        String token = "invalid.token";
        accessor.setNativeHeader("Authorization", "Bearer " + token);
        accessor.setLeaveMutable(true);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        when(tokenService.getSubject(token)).thenThrow(new RuntimeException("Invalid JWT"));

        assertThrows(SecurityException.class, () -> webSocketAuthInterceptor.preSend(message, channel));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Interceptar conexão com usuário não encontrado")
    void preSendWithUserNotFound() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.CONNECT);
        String token = "valid.token";
        String username = "non.existent.user";
        accessor.setNativeHeader("Authorization", "Bearer " + token);
        accessor.setLeaveMutable(true);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        when(tokenService.getSubject(token)).thenReturn(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> webSocketAuthInterceptor.preSend(message, channel));
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    @DisplayName("Ignorar comandos que não são CONNECT")
    void preSendWithNonConnectCommand() {
        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.SUBSCRIBE);
        accessor.setNativeHeader("Authorization", "Bearer some.token");
        accessor.setLeaveMutable(true);
        Message<?> message = MessageBuilder.createMessage(new byte[0], accessor.getMessageHeaders());

        webSocketAuthInterceptor.preSend(message, channel);

        verify(tokenService, never()).getSubject(any());
        assertNull(accessor.getUser());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
