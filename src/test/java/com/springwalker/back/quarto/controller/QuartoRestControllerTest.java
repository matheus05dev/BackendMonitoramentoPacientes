package com.springwalker.back.quarto.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.core.auth.services.TokenService; // Import TokenService
import com.springwalker.back.core.config.security.SecurityConfig;
import com.springwalker.back.core.config.security.SecurityFilter;
import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.service.*;
import com.springwalker.back.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(QuartoRestController.class)
@Import(SecurityConfig.class)
class QuartoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuscaQuartoService buscaQuartoService;

    @MockitoBean
    private CriaQuartoService criaQuartoService;

    @MockitoBean
    private DeletaQuartoService deletaQuartoService;

    @MockitoBean
    private AlteraQuartoService alteraQuartoService;

    @MockitoBean
    private AtribuiPacienteQuartoService atribuiPacienteQuartoService;

    @MockitoBean
    private RemovePacienteQuartoService removePacienteQuartoService;

    @MockitoBean
    private QuartoMapper quartoMapper;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private SecurityFilter securityFilter;

    @MockitoBean
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    private QuartoResponseDTO quartoResponseDTO;
    private QuartoRequestDTO quartoRequestDTO;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        quartoResponseDTO = new QuartoResponseDTO();
        quartoResponseDTO.setId(1L);
        quartoResponseDTO.setNumero(101);
        quartoResponseDTO.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quartoResponseDTO.setTipo(TipoQuarto.UTI);
        quartoResponseDTO.setCapacidade(1);

        quartoRequestDTO = new QuartoRequestDTO();
        quartoRequestDTO.setNumero(101);
        quartoRequestDTO.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quartoRequestDTO.setTipo(TipoQuarto.UTI);
        quartoRequestDTO.setCapacidade(1);

        // Configure the mocked securityFilter to just pass through
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            FilterChain filterChain = invocation.getArgument(2);
            filterChain.doFilter(request, response);
            return null;
        }).when(securityFilter).doFilter(any(ServletRequest.class), any(ServletResponse.class), any(FilterChain.class));
    }

    @Test
    @DisplayName("Deve listar todos os quartos")
    @WithMockUser(roles = "ADMIN")
    void shouldListAllQuartos() throws Exception {
        when(buscaQuartoService.listarTodos()).thenReturn(Collections.singletonList(quartoResponseDTO));

        mockMvc.perform(get("/api/quarto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    @DisplayName("Deve encontrar um quarto por ID")
    @WithMockUser(roles = "ADMIN")
    void shouldFindQuartoById() throws Exception {
        when(buscaQuartoService.buscarPorId(1L)).thenReturn(quartoResponseDTO);

        mockMvc.perform(get("/api/quarto/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve criar um quarto")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateQuarto() throws Exception {
        when(criaQuartoService.inserir(any(QuartoRequestDTO.class))).thenReturn(quartoResponseDTO);

        mockMvc.perform(post("/api/quarto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quartoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve atualizar um quarto")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateQuarto() throws Exception {
        when(alteraQuartoService.alterar(eq(1L), any(QuartoRequestDTO.class))).thenReturn(quartoResponseDTO);

        mockMvc.perform(put("/api/quarto/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quartoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("Deve deletar um quarto")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteQuarto() throws Exception {
        mockMvc.perform(delete("/api/quarto/1"))
                .andExpect(status().isNoContent());
    }
}
