package com.springwalker.back.user.service;

import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.mapper.UserMapper;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AlteraUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AlteraUserService alteraUserService;

    private AutoCloseable closeable;

    @BeforeEach
    void setup() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Alterar usuário com sucesso sem atualizar a senha")
    void executeWithoutPasswordUpdate() {
        UserRequestDTO requestDTO = new UserRequestDTO("updated.user", null, Role.ADMIN);
        User existingUser = new User("original.user", "encodedPassword", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "updated.user", Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toResponseDTO(existingUser)).thenReturn(responseDTO);

        UserResponseDTO result = alteraUserService.execute(1L, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Alterar usuário com sucesso atualizando a senha")
    void executeWithPasswordUpdate() {
        UserRequestDTO requestDTO = new UserRequestDTO("updated.user", "newPassword", Role.ADMIN);
        User existingUser = new User("original.user", "encodedPassword", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "updated.user", Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(userMapper.toResponseDTO(existingUser)).thenReturn(responseDTO);

        UserResponseDTO result = alteraUserService.execute(1L, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO, result);
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Lançar exceção quando o usuário não for encontrado")
    void executeUserNotFound() {
        UserRequestDTO requestDTO = new UserRequestDTO("updated.user", "newPassword", Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> alteraUserService.execute(1L, requestDTO));
        verify(userRepository, never()).save(any(User.class));
    }
}
