package com.springwalker.back.user.service;

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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BuscarUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private BuscarUserService buscarUserService;

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
    @DisplayName("Buscar todos os usuários com sucesso")
    void buscarTodos() {
        User user = new User("test.user", "password", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        List<UserResponseDTO> result = buscarUserService.buscarTodos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(responseDTO, result.get(0));
    }

    @Test
    @DisplayName("Buscar todos os usuários quando não há usuários")
    void buscarTodosEmpty() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = buscarUserService.buscarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Buscar usuário por ID com sucesso")
    void buscarPorId() {
        User user = new User("test.user", "password", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        Optional<UserResponseDTO> result = buscarUserService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Buscar usuário por ID quando não encontrado")
    void buscarPorIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = buscarUserService.buscarPorId(1L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Buscar usuário por nome de usuário com sucesso")
    void buscarPorUsername() {
        User user = new User("test.user", "password", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(userRepository.findByUsername("test.user")).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(responseDTO);

        Optional<UserResponseDTO> result = buscarUserService.buscarPorUsername("test.user");

        assertTrue(result.isPresent());
        assertEquals(responseDTO, result.get());
    }

    @Test
    @DisplayName("Buscar usuário por nome de usuário quando não encontrado")
    void buscarPorUsernameNotFound() {
        when(userRepository.findByUsername("test.user")).thenReturn(Optional.empty());

        Optional<UserResponseDTO> result = buscarUserService.buscarPorUsername("test.user");

        assertFalse(result.isPresent());
    }
}
