package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.repository.QuartoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaQuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @InjectMocks
    private DeletaQuartoService deletaQuartoService;

    @Test
    @DisplayName("Deve excluir um quarto com sucesso")
    void shouldDeleteQuartoSuccessfully() {
        when(quartoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(quartoRepository).deleteById(1L);

        deletaQuartoService.excluir(1L);

        verify(quartoRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao excluir quarto inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentQuarto() {
        when(quartoRepository.existsById(999L)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            deletaQuartoService.excluir(999L);
        });

        assertEquals("Quarto não encontrado com o ID: 999", exception.getMessage());
        verify(quartoRepository, never()).deleteById(999L);
    }
}
