package com.springwalker.back.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.config.security.SecurityConfig;
import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.user.dto.UserRequestDTO;
import com.springwalker.back.user.dto.UserResponseDTO;
import com.springwalker.back.user.repository.UserRepository;
import com.springwalker.back.user.role.Role;
import com.springwalker.back.user.service.AlteraUserService;
import com.springwalker.back.user.service.BuscarUserService;
import com.springwalker.back.user.service.CriarUserService;
import com.springwalker.back.user.service.DeletarUserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserRestController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriarUserService criarUserService;

    @MockitoBean
    private AlteraUserService alteraUserService;

    @MockitoBean
    private BuscarUserService buscarUserService;

    @MockitoBean
    private DeletarUserService deletarUserService;

    // Mocks necessários para o SecurityFilter e SecurityConfig
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Criar usuário com sucesso")
    void createUser() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("test.user", "password", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(criarUserService.execute(any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Changed to isCreated()
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test.user"));
    }

    @Test
    @DisplayName("Alterar usuário com sucesso")
    void updateUser() throws Exception {
        UserRequestDTO requestDTO = new UserRequestDTO("updated.user", "newPassword", Role.ADMIN);
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "updated.user", Role.ADMIN);

        when(alteraUserService.execute(any(Long.class), any(UserRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/users/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("updated.user"));
    }

    @Test
    @DisplayName("Buscar todos os usuários com sucesso")
    void getAllUsers() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(buscarUserService.buscarTodos()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("test.user"));
    }

    @Test
    @DisplayName("Buscar usuário por ID com sucesso")
    void getUserById() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(buscarUserService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/users/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test.user"));
    }

    @Test
    @DisplayName("Buscar usuário por nome de usuário com sucesso")
    void getUserByUsername() throws Exception {
        UserResponseDTO responseDTO = new UserResponseDTO(1L, "test.user", Role.ADMIN);

        when(buscarUserService.buscarPorUsername("test.user")).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/users/username/test.user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test.user"));
    }

    @Test
    @DisplayName("Deletar usuário com sucesso")
    void deleteUser() throws Exception {
        doNothing().when(deletarUserService).execute(1L);

        mockMvc.perform(delete("/api/users/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
