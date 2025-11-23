package com.springwalker.back.paciente.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.core.config.security.SecurityConfig;
import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.service.AlteraPacienteService;
import com.springwalker.back.paciente.service.BuscaPacienteService;
import com.springwalker.back.paciente.service.CriaPacienteService;
import com.springwalker.back.paciente.service.DeletaPacienteService;
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

@WebMvcTest(PacienteRestController.class)
@Import(SecurityConfig.class)
@WithMockUser(roles = "ADMIN")
class PacientesRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CriaPacienteService criaPacienteService;

    @MockitoBean
    private BuscaPacienteService buscaPacienteService;

    @MockitoBean
    private AlteraPacienteService alteraPacienteService;

    @MockitoBean
    private DeletaPacienteService deletaPacienteService;

    @MockitoBean
    private LogService logService;

    // Mocks necessários para o SecurityFilter e SecurityConfig
    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // CPF válido para testes
    private static final String VALID_CPF = "690.438.980-07";

    private PacienteRequestDTO createPacienteRequestDTO() {
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setEmail("joao.silva@example.com");
        requestDTO.setSexo(Sexo.Masculino);
        requestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        requestDTO.setCpf(VALID_CPF); // Usando CPF válido
        requestDTO.setTelefones(Collections.emptyList()); // Assumindo lista vazia para o teste
        requestDTO.setAlergias(List.of("Alergia a amendoim")); // Exemplo de alergia
        requestDTO.setQuartoId(1L); // Exemplo de ID de quarto
        return requestDTO;
    }

    private PacienteResponseDTO createPacienteResponseDTO(Long id) {
        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.setId(id);
        responseDTO.setNome("João Silva");
        responseDTO.setEmail("joao.silva@example.com");
        responseDTO.setSexo(Sexo.Masculino);
        responseDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        responseDTO.setCpf(VALID_CPF); // Usando CPF válido
        responseDTO.setTelefones(Collections.emptyList()); // Assumindo lista vazia para o teste
        responseDTO.setAlergias(List.of("Alergia a amendoim")); // Exemplo de alergia
        responseDTO.setQuartoId(1L); // Exemplo de ID de quarto
        return responseDTO;
    }

    @Test
    @DisplayName("Criar paciente com sucesso")
    void criarPaciente_success() throws Exception {
        PacienteRequestDTO requestDTO = createPacienteRequestDTO();
        PacienteResponseDTO responseDTO = createPacienteResponseDTO(1L);

        when(criaPacienteService.execute(any(PacienteRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.cpf").value(VALID_CPF));
    }

    @Test
    @DisplayName("Criar paciente com CPF duplicado deve retornar bad request")
    void criarPaciente_duplicateCpf_badRequest() throws Exception {
        PacienteRequestDTO requestDTO = createPacienteRequestDTO();

        // Agora que o DTO é válido, o serviço pode lançar a exceção de CPF duplicado
        when(criaPacienteService.execute(any(PacienteRequestDTO.class)))
                .thenThrow(new IllegalArgumentException("CPF já cadastrado."));

        mockMvc.perform(post("/api/pacientes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("CPF já cadastrado."));
    }

    @Test
    @DisplayName("Listar todos os pacientes com sucesso")
    void listarTodosPacientes_success() throws Exception {
        PacienteResponseDTO responseDTO = createPacienteResponseDTO(1L);

        when(buscaPacienteService.listarTodos()).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/pacientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Buscar paciente por ID com sucesso")
    void buscarPacientePorId_success() throws Exception {
        PacienteResponseDTO responseDTO = createPacienteResponseDTO(1L);

        when(buscaPacienteService.buscarPorId(1L)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/pacientes/id/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Buscar paciente por ID não encontrado")
    void buscarPacientePorId_notFound() throws Exception {
        when(buscaPacienteService.buscarPorId(anyLong())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacientes/id/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar paciente por CPF com sucesso")
    void buscarPacientePorCpf_success() throws Exception {
        PacienteResponseDTO responseDTO = createPacienteResponseDTO(1L);

        when(buscaPacienteService.buscarPorCpf(VALID_CPF)).thenReturn(Optional.of(responseDTO));

        mockMvc.perform(get("/api/pacientes/cpf/" + VALID_CPF))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cpf").value(VALID_CPF));
    }

    @Test
    @DisplayName("Buscar paciente por CPF não encontrado")
    void buscarPacientePorCpf_notFound() throws Exception {
        when(buscaPacienteService.buscarPorCpf(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/pacientes/cpf/999.999.999-99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Buscar pacientes por nome com sucesso")
    void buscarPacientePorNome_success() throws Exception {
        PacienteResponseDTO responseDTO = createPacienteResponseDTO(1L);

        when(buscaPacienteService.buscarPorNome("João Silva")).thenReturn(Collections.singletonList(responseDTO));

        mockMvc.perform(get("/api/pacientes/nome/João Silva"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Alterar paciente não encontrado")
    void alterarPaciente_notFound() throws Exception {
        PacienteRequestDTO requestDTO = createPacienteRequestDTO(); // Usando CPF válido

        when(alteraPacienteService.execute(anyLong(), any(PacienteRequestDTO.class)))
                .thenThrow(new NoSuchElementException("Paciente não encontrado")); // Alterado para NoSuchElementException

        mockMvc.perform(put("/api/pacientes/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deletar paciente com sucesso")
    void deletarPaciente_success() throws Exception {
        doNothing().when(deletaPacienteService).execute(1L);

        mockMvc.perform(delete("/api/pacientes/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deletar paciente não encontrado")
    void deletarPaciente_notFound() throws Exception {
        doThrow(new NoSuchElementException("Paciente não encontrado")) // Alterado para NoSuchElementException
                .when(deletaPacienteService).execute(anyLong());

        mockMvc.perform(delete("/api/pacientes/999")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
