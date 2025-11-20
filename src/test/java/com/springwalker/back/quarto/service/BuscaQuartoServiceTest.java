package com.springwalker.back.quarto.service;

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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuscaQuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private QuartoMapper quartoMapper;

    @InjectMocks
    private BuscaQuartoService buscaQuartoService;

    private Quarto quarto;
    private QuartoResponseDTO quartoResponseDTO;

    @BeforeEach
    void setUp() {
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
    @DisplayName("Deve listar todos os quartos com sucesso")
    void shouldListAllQuartosSuccessfully() {
        when(quartoRepository.findAll()).thenReturn(Collections.singletonList(quarto));
        when(quartoMapper.toResponseDTO(any(Quarto.class))).thenReturn(quartoResponseDTO);

        List<QuartoResponseDTO> result = buscaQuartoService.listarTodos();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(quartoResponseDTO, result.get(0));
    }

    @Test
    @DisplayName("Deve buscar um quarto por ID com sucesso")
    void shouldFindQuartoByIdSuccessfully() {
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(quartoMapper.toResponseDTO(any(Quarto.class))).thenReturn(quartoResponseDTO);

        QuartoResponseDTO result = buscaQuartoService.buscarPorId(1L);

        assertNotNull(result);
        assertEquals(quartoResponseDTO, result);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar quarto por ID inexistente")
    void shouldThrowExceptionWhenQuartoNotFoundById() {
        when(quartoRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            buscaQuartoService.buscarPorId(999L);
        });

        assertEquals("Quarto não encontrado com o ID: 999", exception.getMessage());
    }
}
