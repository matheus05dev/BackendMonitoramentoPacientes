package com.springwalker.back.user.service;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.mapper.UserMapper;
import com.springwalker.back.user.model.User;
import com.springwalker.back.user.repository.UserRepository;
import com.springwalker.back.user.role.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CriarUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CriarUserService criarUserService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar usuário com sucesso")
    void execute() {
        UserRequestDTO requestDTO = new UserRequestDTO("test.user", "password", Role.ADMIN);
        User user = new User("test.user", "encodedPassword", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(userRepository.findByUsername("test.user")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        UserResponseDTO result = criarUserService.execute(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Lançar exceção quando o nome de usuário já existe")
    void executeWhenUsernameExists() {
        UserRequestDTO requestDTO = new UserRequestDTO("test.user", "password", Role.ADMIN);

        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class, () -> criarUserService.execute(requestDTO));
        verify(userRepository, never()).save(any(User.class));
    }
}
