package com.springwalker.back.core.auth.services;

import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Carregar usuário por nome de usuário com sucesso")
    void loadUserByUsername() {
        User user = new User();
        user.setUsername("test.user");
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(user));

        UserDetails userDetails = authenticationService.loadUserByUsername("test.user");

        assertNotNull(userDetails);
        assertEquals("test.user", userDetails.getUsername());
    }

    @Test
    @DisplayName("Lançar exceção quando o usuário não for encontrado")
    void loadUserByUsernameNotFound() {
        when(userRepository.findByUsername("non.existent.user")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> authenticationService.loadUserByUsername("non.existent.user"));
    }
}
