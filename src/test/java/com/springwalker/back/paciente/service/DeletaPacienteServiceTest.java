package com.springwalker.back.paciente.service;

import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaPacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private AtendimentoRepository atendimentoRepository;

    @InjectMocks
    private DeletaPacienteService deletaPacienteService;

    @Test
    @DisplayName("Deve deletar um paciente com sucesso quando o paciente existir")
    void shouldDeletePatientSuccessfullyWhenPatientExists() {
        Long pacienteId = 1L;
        when(pacienteRepository.existsById(pacienteId)).thenReturn(true);

        deletaPacienteService.execute(pacienteId);

        verify(pacienteRepository, times(1)).existsById(pacienteId);
        verify(atendimentoRepository, times(1)).desvincularPaciente(pacienteId);
        verify(pacienteRepository, times(1)).deleteById(pacienteId);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando o paciente a ser deletado não existir")
    void shouldThrowExceptionWhenPatientToDeleteDoesNotExist() {
        Long pacienteId = 1L;
        when(pacienteRepository.existsById(pacienteId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            deletaPacienteService.execute(pacienteId);
        });

        assertEquals("Paciente não encontrado com ID: " + pacienteId, exception.getMessage());
        verify(pacienteRepository, times(1)).existsById(pacienteId);
        verify(atendimentoRepository, never()).desvincularPaciente(anyLong());
        verify(pacienteRepository, never()).deleteById(anyLong());
    }
}
