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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaAtendimentoServiceTest {

    @Mock
    private AtendimentoRepository atendimentoRepository;

    @Mock
    private AtendimentoMapper atendimentoMapper;

    @InjectMocks
    private BuscaAtendimentoService buscaAtendimentoService;

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
    @DisplayName("Deve buscar um atendimento por ID com sucesso")
    void shouldFindAtendimentoByIdSuccessfully() {
        when(atendimentoRepository.findById(1L)).thenReturn(Optional.of(atendimento));
        when(atendimentoMapper.toResponseDTO(any(Atendimento.class))).thenReturn(atendimentoResponseDTO);

        Optional<AtendimentoResponseDTO> result = buscaAtendimentoService.buscarAtendimentoPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(atendimentoResponseDTO, result.get());
    }

    @Test
    @DisplayName("Deve retornar vazio ao buscar atendimento por ID inexistente")
    void shouldReturnEmptyWhenAtendimentoNotFoundById() {
        when(atendimentoRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<AtendimentoResponseDTO> result = buscaAtendimentoService.buscarAtendimentoPorId(999L);

        assertFalse(result.isPresent());
    }

    @Test
    @DisplayName("Deve buscar todos os atendimentos com sucesso")
    void shouldFindAllAtendimentosSuccessfully() {
        when(atendimentoRepository.findAll()).thenReturn(Collections.singletonList(atendimento));
        when(atendimentoMapper.toResponseDTO(any(Atendimento.class))).thenReturn(atendimentoResponseDTO);

        List<AtendimentoResponseDTO> result = buscaAtendimentoService.buscarTodosAtendimentos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(atendimentoResponseDTO, result.get(0));
    }
}
