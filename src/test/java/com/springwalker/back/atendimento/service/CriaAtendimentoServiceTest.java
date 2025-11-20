package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.mapper.AtendimentoMapper;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriaAtendimentoServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;
    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private FuncionarioSaudeRepository funcionarioSaudeRepository;
    @Mock
    private AtendimentoMapper atendimentoMapper;

    @InjectMocks
    private CriaAtendimentoService criaAtendimentoService;

    private AtendimentoRequestDTO atendimentoRequestDTO;
    private Paciente paciente;
    private FuncionarioSaude medico;
    private Atendimento atendimento;
    private AtendimentoResponseDTO atendimentoResponseDTO;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("Paciente Teste");

        medico = new FuncionarioSaude();
        medico.setId(1L);
        medico.setNome("Dr. Teste");
        medico.setCargo(Cargo.MEDICO);

        atendimentoRequestDTO = new AtendimentoRequestDTO();
        atendimentoRequestDTO.setPacienteId(1L);
        atendimentoRequestDTO.setMedicoResponsavelId(1L);
        atendimentoRequestDTO.setDiagnostico(Diagnostico.A41);

        atendimento = new Atendimento();
        atendimentoResponseDTO = new AtendimentoResponseDTO();
    }

    @Test
    @DisplayName("Deve criar um atendimento com sucesso")
    void shouldCreateAtendimentoSuccessfully() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(atendimentoRepository.existsByPacienteIdAndDataSaidaIsNull(1L)).thenReturn(false);
        when(funcionarioSaudeRepository.findById(1L)).thenReturn(Optional.of(medico));
        when(atendimentoMapper.toEntity(any(AtendimentoRequestDTO.class))).thenReturn(atendimento);
        when(atendimentoRepository.save(any(Atendimento.class))).thenReturn(atendimento);
        when(atendimentoMapper.toResponseDTO(any(Atendimento.class))).thenReturn(atendimentoResponseDTO);

        AtendimentoResponseDTO result = criaAtendimentoService.criarAtendimento(atendimentoRequestDTO);

        assertNotNull(result);
        assertEquals(atendimentoResponseDTO, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar atendimento para paciente com atendimento em aberto")
    void shouldThrowExceptionWhenPacienteHasOpenAtendimento() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(atendimentoRepository.existsByPacienteIdAndDataSaidaIsNull(1L)).thenReturn(true);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            criaAtendimentoService.criarAtendimento(atendimentoRequestDTO);
        });

        assertEquals("O paciente já possui um atendimento em aberto.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção se o responsável não for médico")
    void shouldThrowExceptionWhenResponsavelIsNotMedico() {
        medico.setCargo(Cargo.ENFERMEIRO);
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(funcionarioSaudeRepository.findById(1L)).thenReturn(Optional.of(medico));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criaAtendimentoService.criarAtendimento(atendimentoRequestDTO);
        });

        assertEquals("O funcionário responsável deve ser um médico.", exception.getMessage());
    }
    
    @Test
    @DisplayName("Deve lançar exceção se o paciente não for encontrado")
    void shouldThrowExceptionWhenPacienteNotFound() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            criaAtendimentoService.criarAtendimento(atendimentoRequestDTO);
        });

        assertEquals("Paciente não encontrado", exception.getMessage());
    }
}
