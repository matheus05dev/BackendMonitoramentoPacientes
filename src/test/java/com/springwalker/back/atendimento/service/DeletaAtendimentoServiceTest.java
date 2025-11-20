package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.mapper.AtendimentoMapper;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeletaAtendimentoServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;
    @Mock
    private AtendimentoMapper atendimentoMapper;

    @InjectMocks
    private DeletaAtendimentoService deletaAtendimentoService;

    private Atendimento atendimento;
    private AtendimentoResponseDTO atendimentoResponseDTO;

    @BeforeEach
    void setUp() {
        atendimento = new Atendimento();
        atendimento.setId(1L);

        atendimentoResponseDTO = new AtendimentoResponseDTO();
        atendimentoResponseDTO.setId(1L);
    }

    @Test
    @DisplayName("Deve deletar um atendimento com sucesso")
    void shouldDeleteAtendimentoSuccessfully() {
        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));
        when(atendimentoMapper.toResponseDTO(atendimento)).thenReturn(atendimentoResponseDTO);
        doNothing().when(atendimentoRepository).delete(atendimento);

        AtendimentoResponseDTO result = deletaAtendimentoService.deletarAtendimento(1L);

        assertNotNull(result);
        assertEquals(atendimentoResponseDTO, result);
        verify(atendimentoRepository, times(1)).delete(atendimento);
    }



    @Test
    @DisplayName("Deve lançar exceção ao deletar atendimento inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentAtendimento() {
        when(atendimentoRepository.findById(999L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            deletaAtendimentoService.deletarAtendimento(999L);
        });

        assertEquals("Atendimento não encontrado com o ID: 999", exception.getMessage());
    }
}
