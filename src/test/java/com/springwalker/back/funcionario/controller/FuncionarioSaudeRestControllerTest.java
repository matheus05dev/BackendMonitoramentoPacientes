package com.springwalker.back.funcionario.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.config.security.SecurityConfig;
import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.funcionario.service.AlteraFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.BuscarFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.CriaFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.DeletaFuncionarioSaudeService;
import com.springwalker.back.pessoa.enums.Sexo;
import com.springwalker.back.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FuncionarioSaudeRestController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class FuncionarioSaudeRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;

    @MockitoBean
    private AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;

    @MockitoBean
    private CriaFuncionarioSaudeService criaFuncionarioSaudeService;

    @MockitoBean
    private DeletaFuncionarioSaudeService deletaFuncionarioSaudeService;

    // Mocks necessários para o SecurityFilter e SecurityConfig
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private LogService logService;

    @Autowired
    private ObjectMapper objectMapper;

    private FuncionarioSaudeRequestDTO createRequestDTO() {
        FuncionarioSaudeRequestDTO dto = new FuncionarioSaudeRequestDTO();
        dto.setNome("Funcionario Teste");
        dto.setEmail("teste@example.com");
        dto.setCpf("62231975009"); // CPF válido
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setSexo(Sexo.Masculino);
        dto.setCargo(Cargo.MEDICO);
        dto.setEspecialidades(List.of("Cardiologia"));
        dto.setIdentificacao("CRM123");
        dto.setTelefones(Collections.emptyList());
        return dto;
    }

    private FuncionarioSaudeResponseDTO createResponseDTO(Long id) {
        FuncionarioSaudeResponseDTO dto = new FuncionarioSaudeResponseDTO();
        dto.setId(id);
        dto.setNome("Funcionario Teste");
        dto.setEmail("teste@example.com");
        dto.setCpf("93702956077"); // CPF válido
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setSexo(Sexo.Masculino);
        dto.setCargo(Cargo.MEDICO);
        dto.setEspecialidades(List.of("Cardiologia"));
        dto.setIdentificacao("CRM123");
        dto.setTelefones(Collections.emptyList());
        dto.setAtendimentosIds(Collections.emptyList());
        dto.setAtendimentosComplicacaoIds(Collections.emptyList());
        return dto;
    }

    @Test
    @DisplayName("Deve listar todos os funcionários com sucesso")
    void listar_success() throws Exception {
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);
        when(buscarFuncionarioSaudeService.listarTodos()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/funcionario"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Funcionario Teste"));
    }

    @Test
    @DisplayName("Deve criar um novo funcionário com sucesso")
    void inserir_success() throws Exception {
        FuncionarioSaudeRequestDTO requestDTO = createRequestDTO();
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);

        when(criaFuncionarioSaudeService.execute(any(FuncionarioSaudeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/funcionario")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated()) // Changed to isCreated (HTTP 201)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Funcionario Teste"));
    }

    @Test
    @DisplayName("Deve retornar bad request ao criar funcionário com CPF duplicado")
    void inserir_duplicateCpf_badRequest() throws Exception {
        FuncionarioSaudeRequestDTO requestDTO = createRequestDTO();

        when(criaFuncionarioSaudeService.execute(any(FuncionarioSaudeRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("CPF já cadastrado."));

        mockMvc.perform(post("/api/funcionario")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("CPF já cadastrado.")); // Corrigido o jsonPath
    }

    @Test
    @DisplayName("Deve alterar um funcionário com sucesso")
    void alterar_success() throws Exception {
        FuncionarioSaudeRequestDTO requestDTO = createRequestDTO();
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);
        responseDTO.setNome("Nome Alterado");

        when(alteraFuncionarioSaudeService.execute(anyLong(), any(FuncionarioSaudeRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/funcionario/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Nome Alterado"));
    }

    @Test
    @DisplayName("Deve retornar not found ao tentar alterar funcionário inexistente")
    void alterar_notFound() throws Exception {
        FuncionarioSaudeRequestDTO requestDTO = createRequestDTO();

        when(alteraFuncionarioSaudeService.execute(anyLong(), any(FuncionarioSaudeRequestDTO.class)))
                .thenThrow(new NoSuchElementException("Funcionário não encontrado")); // Changed to NoSuchElementException

        mockMvc.perform(put("/api/funcionario/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve excluir um funcionário com sucesso")
    void excluir_success() throws Exception {
        doNothing().when(deletaFuncionarioSaudeService).execute(1L);

        mockMvc.perform(delete("/api/funcionario/1")
                        .with(csrf()))
                .andExpect(status().isNoContent()); // Alterado para isNoContent (HTTP 204)
    }

    @Test
    @DisplayName("Deve retornar not found ao tentar excluir funcionário inexistente")
    void excluir_notFound() throws Exception {
        doThrow(new NoSuchElementException("Funcionário não encontrado")) // Changed to NoSuchElementException
                .when(deletaFuncionarioSaudeService).execute(anyLong());

        mockMvc.perform(delete("/api/funcionario/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar funcionário por ID com sucesso")
    void buscarPorId_success() throws Exception {
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);
        when(buscarFuncionarioSaudeService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/funcionario/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Funcionario Teste"));
    }

    @Test
    @DisplayName("Deve retornar not found ao buscar funcionário por ID inexistente")
    void buscarPorId_notFound() throws Exception {
        when(buscarFuncionarioSaudeService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/funcionario/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve buscar funcionários por nome com sucesso")
    void buscarPorNome_success() throws Exception {
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);
        when(buscarFuncionarioSaudeService.buscarPorNome("Funcionario Teste")).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/funcionario/buscar-por-nome/Funcionario Teste"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Funcionario Teste"));
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar funcionários por nome inexistente")
    void buscarPorNome_emptyList() throws Exception {
        when(buscarFuncionarioSaudeService.buscarPorNome(anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/funcionario/buscar-por-nome/Nome Inexistente"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Deve buscar funcionário por CPF com sucesso")
    void buscarPorCpf_success() throws Exception {
        FuncionarioSaudeResponseDTO responseDTO = createResponseDTO(1L);
        // Ensure the CPF in the responseDTO matches the one being searched for
        responseDTO.setCpf("111.444.777-05");
        when(buscarFuncionarioSaudeService.buscarPorCpf("111.444.777-05")).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/funcionario/buscar-por-cpf/111.444.777-05"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value("111.444.777-05"));
    }

    @Test
    @DisplayName("Deve retornar not found ao buscar funcionário por CPF inexistente")
    void buscarPorCpf_notFound() throws Exception {
        when(buscarFuncionarioSaudeService.buscarPorCpf(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/funcionario/buscar-por-cpf/999.999.999-99"))
                .andExpect(status().isNotFound());
    }
}
