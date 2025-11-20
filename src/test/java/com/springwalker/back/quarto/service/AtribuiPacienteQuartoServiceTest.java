package com.springwalker.back.quarto.service;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AtribuiPacienteQuartoServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private QuartoRepository quartoRepository;
    @Mock
    private AtendimentoRepository atendimentoRepository;

    @InjectMocks
    private AtribuiPacienteQuartoService atribuiPacienteQuartoService;

    private Quarto quarto;
    private Paciente paciente;
    private Atendimento atendimento;

    @BeforeEach
    void setUp() {
        quarto = Quarto.builder()
                .id(1L)
                .numero(101)
                .capacidade(1)
                .build();

        paciente = new Paciente();
        paciente.setId(1L);

        atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setPaciente(paciente);
    }

    @Test
    @DisplayName("Deve alocar um paciente a um quarto com sucesso")
    void shouldAllocatePacienteToQuartoSuccessfully() {
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(atendimentoRepository.findByPacienteIdAndDataSaidaIsNull(1L)).thenReturn(Optional.of(atendimento));

        Quarto result = atribuiPacienteQuartoService.alocarPaciente(1L, 1L);

        assertNotNull(result);
        assertEquals(quarto, result);
        assertEquals(quarto, paciente.getQuarto());
        assertEquals(quarto, atendimento.getQuarto());
        assertEquals(quarto.getNumero(), atendimento.getNumeroQuarto());

        verify(pacienteRepository, times(1)).save(paciente);
        verify(atendimentoRepository, times(1)).save(atendimento);
    }

    @Test
    @DisplayName("Deve lançar exceção ao alocar paciente em quarto lotado")
    void shouldThrowExceptionWhenAllocatingPacienteToFullQuarto() {
        quarto.adicionarPaciente(new Paciente()); // Quarto já está cheio
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            atribuiPacienteQuartoService.alocarPaciente(1L, 1L);
        });

        assertEquals("Quarto 101 já atingiu a capacidade máxima de 1 pacientes.", exception.getMessage());
    }
}
