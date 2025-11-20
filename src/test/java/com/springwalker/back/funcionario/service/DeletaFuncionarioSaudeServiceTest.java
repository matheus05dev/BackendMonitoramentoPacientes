package com.springwalker.back.funcionario.service;

import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaFuncionarioSaudeServiceTest {

    @Mock
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Mock
    private AtendimentoRepository atendimentoRepository;

    @InjectMocks
    private DeletaFuncionarioSaudeService deletaFuncionarioSaudeService;

    private final Long funcionarioId = 1L;

    @Test
    @DisplayName("Deve deletar FuncionarioSaude com sucesso quando ele existir")
    void shouldDeleteFuncionarioSaudeSuccessfullyWhenItExists() {
        when(funcionarioSaudeRepository.existsById(funcionarioId)).thenReturn(true);
        doNothing().when(atendimentoRepository).desvincularMedicoResponsavel(funcionarioId);
        doNothing().when(atendimentoRepository).desvincularMedicoComplicacao(funcionarioId);
        doNothing().when(funcionarioSaudeRepository).deleteById(funcionarioId);

        deletaFuncionarioSaudeService.execute(funcionarioId);

        verify(funcionarioSaudeRepository, times(1)).existsById(funcionarioId);
        verify(atendimentoRepository, times(1)).desvincularMedicoResponsavel(funcionarioId);
        verify(atendimentoRepository, times(1)).desvincularMedicoComplicacao(funcionarioId);
        verify(funcionarioSaudeRepository, times(1)).deleteById(funcionarioId);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando FuncionarioSaude não existir")
    void shouldThrowRuntimeExceptionWhenFuncionarioSaudeDoesNotExist() {
        when(funcionarioSaudeRepository.existsById(funcionarioId)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                deletaFuncionarioSaudeService.execute(funcionarioId));

        assertEquals("Funcionário não encontrado com ID: " + funcionarioId, exception.getMessage());
        verify(funcionarioSaudeRepository, times(1)).existsById(funcionarioId);
        verify(atendimentoRepository, never()).desvincularMedicoResponsavel(anyLong());
        verify(atendimentoRepository, never()).desvincularMedicoComplicacao(anyLong());
        verify(funcionarioSaudeRepository, never()).deleteById(anyLong());
    }
}
