package com.springwalker.back.atendimento.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.enums.StatusMonitoramento;
import com.springwalker.back.atendimento.enums.StatusPaciente;
import com.springwalker.back.atendimento.service.AlteraAtendimentoService;
import com.springwalker.back.atendimento.service.BuscaAtendimentoService;
import com.springwalker.back.atendimento.service.CriaAtendimentoService;
import com.springwalker.back.atendimento.service.DeletaAtendimentoService;
import com.springwalker.back.core.auth.services.TokenService;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AtendimentoRestController.class)
class AtendimentoRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AlteraAtendimentoService alteraAtendimentoService;
    @MockitoBean
    private CriaAtendimentoService criaAtendimentoService;
    @MockitoBean
    private BuscaAtendimentoService buscaAtendimentoService;
    @MockitoBean
    private DeletaAtendimentoService deletaAtendimentoService;
    @MockitoBean
    private PacienteRepository pacienteRepository;
    @MockitoBean
    private FuncionarioSaudeRepository funcionarioSaudeRepository;
    @MockitoBean
    private UserRepository userRepository;
    @MockitoBean
    private TokenService tokenService;



    @Autowired
    private ObjectMapper objectMapper;

    private AtendimentoRequestDTO atendimentoRequestDTO;
    private AtendimentoResponseDTO atendimentoResponseDTO;

    @BeforeEach
    void setUp() {
        atendimentoRequestDTO = new AtendimentoRequestDTO();
        atendimentoRequestDTO.setPacienteId(1L);
        atendimentoRequestDTO.setMedicoResponsavelId(2L);
        atendimentoRequestDTO.setQuartoId(10L);
        atendimentoRequestDTO.setStatusPaciente(StatusPaciente.Internado);
        atendimentoRequestDTO.setStatusMonitoramento(StatusMonitoramento.MONITORANDO);
        atendimentoRequestDTO.setAcompanhante("Maria Silva");
        atendimentoRequestDTO.setCondicoesPreexistentes("Nenhuma");
        atendimentoRequestDTO.setDiagnostico(Diagnostico.A41);
        atendimentoRequestDTO.setTratamento("Repouso e medicação");
        atendimentoRequestDTO.setDataEntrada(LocalDateTime.of(2023, 1, 15, 10, 0));
        atendimentoRequestDTO.setObservacoes("Paciente com febre");
        atendimentoRequestDTO.setDiagnosticoComplicacao(Diagnostico.J12);
        atendimentoRequestDTO.setTratamentoComplicacao("Antibióticos");


        atendimentoResponseDTO = new AtendimentoResponseDTO();
        atendimentoResponseDTO.setId(1L);
        atendimentoResponseDTO.setPacienteId(1L);
        atendimentoResponseDTO.setMedicoResponsavelId(2L);
        atendimentoResponseDTO.setNumeroQuarto(101);
        atendimentoResponseDTO.setStatusPaciente(StatusPaciente.Internado);
        atendimentoResponseDTO.setStatusMonitoramento(StatusMonitoramento.MONITORANDO);
        atendimentoResponseDTO.setAcompanhante("Maria Silva");
        atendimentoResponseDTO.setCondicoesPreexistentes("Nenhuma");
        atendimentoResponseDTO.setDiagnostico(Diagnostico.A41);
        atendimentoResponseDTO.setTratamento("Repouso e medicação");
        atendimentoResponseDTO.setDataEntrada(LocalDateTime.of(2023, 1, 15, 10, 0));
        atendimentoResponseDTO.setObservacoes("Paciente com febre");
        atendimentoResponseDTO.setNomePaciente("João Silva");
        atendimentoResponseDTO.setNomeMedicoResponsavel("Dr. Carlos");
        atendimentoResponseDTO.setDiagnosticoComplicacao(Diagnostico.J12);
        atendimentoResponseDTO.setTratamentoComplicacao("Antibióticos");
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MEDICO"}) // Simula um usuário com as roles necessárias
    void criaAtendimento() throws Exception {
        when(criaAtendimentoService.criarAtendimento(any(AtendimentoRequestDTO.class))).thenReturn(atendimentoResponseDTO);

        mockMvc.perform(post("/api/atendimento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimentoRequestDTO))
                .with(csrf())) // Adicionado para CSRF
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(atendimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.pacienteId").value(atendimentoResponseDTO.getPacienteId()))
                .andExpect(jsonPath("$.nomePaciente").value(atendimentoResponseDTO.getNomePaciente()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MEDICO", "ENFERMEIRO", "AUXILIAR_ENFERMAGEM", "TECNICO_ENFERMAGEM", "ESTAGIARIO"})
    void buscarAtendimentoPorId() throws Exception {
        when(buscaAtendimentoService.buscarAtendimentoPorId(1L)).thenReturn(Optional.of(atendimentoResponseDTO));

        mockMvc.perform(get("/api/atendimento/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(atendimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.pacienteId").value(atendimentoResponseDTO.getPacienteId()))
                .andExpect(jsonPath("$.nomePaciente").value(atendimentoResponseDTO.getNomePaciente()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MEDICO", "ENFERMEIRO", "AUXILIAR_ENFERMAGEM", "TECNICO_ENFERMAGEM", "ESTAGIARIO"})
    void buscarAtendimentoPorIdNotFound() throws Exception {
        when(buscaAtendimentoService.buscarAtendimentoPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/atendimento/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MEDICO", "ENFERMEIRO", "AUXILIAR_ENFERMAGEM", "TECNICO_ENFERMAGEM", "ESTAGIARIO"})
    void buscarTodosAtendimentos() throws Exception {
        List<AtendimentoResponseDTO> atendimentos = Arrays.asList(atendimentoResponseDTO);
        when(buscaAtendimentoService.buscarTodosAtendimentos()).thenReturn(atendimentos);

        mockMvc.perform(get("/api/atendimento")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(atendimentoResponseDTO.getId()))
                .andExpect(jsonPath("$[0].pacienteId").value(atendimentoResponseDTO.getPacienteId()))
                .andExpect(jsonPath("$[0].nomePaciente").value(atendimentoResponseDTO.getNomePaciente()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN", "MEDICO"})
    void alterarAtendimento() throws Exception {
        when(alteraAtendimentoService.alterarAtendimento(eq(1L), any(AtendimentoRequestDTO.class))).thenReturn(atendimentoResponseDTO);

        mockMvc.perform(put("/api/atendimento/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(atendimentoRequestDTO))
                .with(csrf())) // Adicionado para CSRF
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(atendimentoResponseDTO.getId()))
                .andExpect(jsonPath("$.pacienteId").value(atendimentoResponseDTO.getPacienteId()))
                .andExpect(jsonPath("$.nomePaciente").value(atendimentoResponseDTO.getNomePaciente()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deletarAtendimento() throws Exception {
        when(deletaAtendimentoService.deletarAtendimento(1L)).thenReturn(atendimentoResponseDTO);

        mockMvc.perform(delete("/api/atendimento/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())) // Adicionado para CSRF
                .andExpect(status().isOk()) // O controller retorna 200 OK com o DTO deletado
                .andExpect(jsonPath("$.id").value(atendimentoResponseDTO.getId()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void deletarAtendimentoNotFound() throws Exception {
        when(deletaAtendimentoService.deletarAtendimento(1L)).thenThrow(new NoSuchElementException());

        mockMvc.perform(delete("/api/atendimento/1")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf())) // Adicionado para CSRF
                .andExpect(status().isNotFound());
    }
}
