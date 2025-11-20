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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlteraQuartoServiceTest {

    @Mock
    private QuartoRepository quartoRepository;

    @Mock
    private QuartoMapper quartoMapper;

    @InjectMocks
    private AlteraQuartoService alteraQuartoService;

    private QuartoRequestDTO quartoRequestDTO;
    private Quarto quarto;
    private QuartoResponseDTO quartoResponseDTO;

    @BeforeEach
    void setUp() {
        quartoRequestDTO = new QuartoRequestDTO();
        quartoRequestDTO.setNumero(102);
        quartoRequestDTO.setLocalizacao(LocalizacaoQuarto.SETOR_SUL);
        quartoRequestDTO.setTipo(TipoQuarto.ENFERMARIA);
        quartoRequestDTO.setCapacidade(2);

        quarto = new Quarto();
        quarto.setId(1L);
        quarto.setNumero(101);
        quarto.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quarto.setTipo(TipoQuarto.UTI);
        quarto.setCapacidade(1);

        quartoResponseDTO = new QuartoResponseDTO();
        quartoResponseDTO.setId(1L);
        quartoResponseDTO.setNumero(102);
        quartoResponseDTO.setLocalizacao(LocalizacaoQuarto.SETOR_SUL);
        quartoResponseDTO.setTipo(TipoQuarto.ENFERMARIA);
        quartoResponseDTO.setCapacidade(2);
    }

    @Test
    @DisplayName("Deve alterar um quarto com sucesso")
    void shouldAlterQuartoSuccessfully() {
        when(quartoRepository.findById(1L)).thenReturn(Optional.of(quarto));
        when(quartoRepository.save(any(Quarto.class))).thenReturn(quarto);
        when(quartoMapper.toResponseDTO(any(Quarto.class))).thenReturn(quartoResponseDTO);

        QuartoResponseDTO result = alteraQuartoService.alterar(1L, quartoRequestDTO);

        assertNotNull(result);
        assertEquals(quartoResponseDTO, result);
        verify(quartoMapper, times(1)).updateFromDto(quartoRequestDTO, quarto);
    }

    @Test
    @DisplayName("Deve lançar exceção ao alterar quarto inexistente")
    void shouldThrowExceptionWhenAlteringNonExistentQuarto() {
        when(quartoRepository.findById(999L)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            alteraQuartoService.alterar(999L, quartoRequestDTO);
        });

        assertEquals("Quarto não encontrado com o ID: 999", exception.getMessage());
    }
}
