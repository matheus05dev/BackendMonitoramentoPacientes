package com.springwalker.back.quarto.service;

import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.repository.QuartoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CriaQuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private QuartoMapper quartoMapper;

    @InjectMocks
    private CriaQuartoService criaQuartoService;

    private QuartoRequestDTO quartoRequestDTO;
    private Quarto quarto;
    private QuartoResponseDTO quartoResponseDTO;

    @BeforeEach
    void setUp() {
        quartoRequestDTO = new QuartoRequestDTO();
        quartoRequestDTO.setNumero(101);
        quartoRequestDTO.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quartoRequestDTO.setTipo(TipoQuarto.UTI);
        quartoRequestDTO.setCapacidade(1);

        quarto = new Quarto();
        quarto.setId(1L);
        quarto.setNumero(101);
        quarto.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quarto.setTipo(TipoQuarto.UTI);
        quarto.setCapacidade(1);

        quartoResponseDTO = new QuartoResponseDTO();
        quartoResponseDTO.setId(1L);
        quartoResponseDTO.setNumero(101);
        quartoResponseDTO.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quartoResponseDTO.setTipo(TipoQuarto.UTI);
        quartoResponseDTO.setCapacidade(1);
    }

    @Test
    @DisplayName("Deve inserir um quarto com sucesso")
    void shouldInsertQuartoSuccessfully() {
        when(quartoMapper.toEntity(any(QuartoRequestDTO.class))).thenReturn(quarto);
        when(quartoRepository.save(any(Quarto.class))).thenReturn(quarto);
        when(quartoMapper.toResponseDTO(any(Quarto.class))).thenReturn(quartoResponseDTO);

        QuartoResponseDTO result = criaQuartoService.inserir(quartoRequestDTO);

        assertNotNull(result);
        assertEquals(quartoResponseDTO, result);
    }

    @Test
    @DisplayName("Deve inserir vários quartos com sucesso")
    void shouldInsertMultipleQuartosSuccessfully() {
        when(quartoMapper.toEntity(any(QuartoRequestDTO.class))).thenReturn(quarto);
        when(quartoRepository.saveAll(any(List.class))).thenReturn(Collections.singletonList(quarto));
        when(quartoMapper.toResponseDTO(any(Quarto.class))).thenReturn(quartoResponseDTO);

        List<QuartoResponseDTO> result = criaQuartoService.inserirVarios(Collections.singletonList(quartoRequestDTO));

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(quartoResponseDTO, result.get(0));
    }
}
