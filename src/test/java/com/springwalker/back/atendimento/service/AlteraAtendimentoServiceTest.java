package com.springwalker.back.atendimento.service;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.enums.StatusPaciente;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlteraAtendimentoServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;
    @Mock
    private AtendimentoMapper atendimentoMapper;

    @InjectMocks
    private AlteraAtendimentoService alteraAtendimentoService;

    private AtendimentoRequestDTO atendimentoRequestDTO;
    private Atendimento atendimento;
    private AtendimentoResponseDTO atendimentoResponseDTO;

    @BeforeEach
    void setUp() {
        atendimento = new Atendimento();
        atendimento.setId(1L);
        atendimento.setStatusPaciente(StatusPaciente.Internado);

        atendimentoRequestDTO = new AtendimentoRequestDTO();
        atendimentoRequestDTO.setStatusPaciente(StatusPaciente.Liberado);

        atendimentoResponseDTO = new AtendimentoResponseDTO();
        atendimentoResponseDTO.setId(1L);
        atendimentoResponseDTO.setStatusPaciente(StatusPaciente.Liberado);
    }

    @Test
    @DisplayName("Deve alterar um atendimento com sucesso")
    void shouldAlterAtendimentoSuccessfully() {
        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));
        when(atendimentoRepository.save(any(Atendimento.class))).thenReturn(atendimento);
        when(atendimentoMapper.toResponseDTO(any(Atendimento.class))).thenReturn(atendimentoResponseDTO);

        AtendimentoResponseDTO result = alteraAtendimentoService.alterarAtendimento(1L, atendimentoRequestDTO);

        assertNotNull(result);
        assertEquals(atendimentoResponseDTO, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar atendimento liberado")
    void shouldThrowExceptionWhenAlteringLiberadoAtendimento() {
        atendimento.setStatusPaciente(StatusPaciente.Liberado);
        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            alteraAtendimentoService.alterarAtendimento(1L, atendimentoRequestDTO);
        });

        assertEquals("Não é permitido alterar um atendimento já liberado.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar atendimento inexistente")
    void shouldThrowExceptionWhenAlteringNonExistentAtendimento() {
        when(atendimentoRepository.findById(999L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            alteraAtendimentoService.alterarAtendimento(999L, atendimentoRequestDTO);
        });

        assertEquals("Atendimento não encontrado com o ID: 999", exception.getMessage());
    }
}
