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
class RemovePacienteQuartoServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private QuartoRepository quartoRepository;
    @Mock
    private AtendimentoRepository atendimentoRepository;

    @InjectMocks
    private RemovePacienteQuartoService removePacienteQuartoService;

    private Quarto quarto;
    private Paciente paciente;
    private Atendimento atendimento;

    @BeforeEach
    void setUp() {
        paciente = new Paciente();
        paciente.setId(1L);

        quarto = Quarto.builder()
                .id(1L)
                .numero(101)
                .capacidade(1)
                .build();
        quarto.adicionarPaciente(paciente);
        paciente.setQuarto(quarto);

        atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setPaciente(paciente);
        atendimento.setQuarto(quarto);
        atendimento.setNumeroQuarto(quarto.getNumero());
    }

    @Test
    @DisplayName("Deve remover um paciente de um quarto com sucesso")
    void shouldRemovePacienteFromQuartoSuccessfully() {
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));
        when(atendimentoRepository.findByPacienteIdAndDataSaidaIsNull(1L)).thenReturn(Optional.of(atendimento));

        Quarto result = removePacienteQuartoService.removerPaciente(1L, 1L);

        assertNotNull(result);
        assertEquals(quarto, result);
        assertNull(paciente.getQuarto());
        assertNull(atendimento.getQuarto());
        assertNull(atendimento.getNumeroQuarto());

        verify(pacienteRepository, times(1)).save(paciente);
        verify(atendimentoRepository, times(1)).save(atendimento);
    }

    @Test
    @DisplayName("Deve lançar exceção ao remover paciente que não está no quarto")
    void shouldThrowExceptionWhenRemovingPacienteNotInQuarto() {
        quarto.removerPaciente(paciente); // Remove o paciente antes do teste
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            removePacienteQuartoService.removerPaciente(1L, 1L);
        });

        assertEquals("Paciente não encontrado no quarto 101", exception.getMessage());
    }
}
