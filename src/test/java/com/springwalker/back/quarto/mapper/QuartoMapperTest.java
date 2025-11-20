package com.springwalker.back.quarto.mapper;

import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QuartoMapperTest {

    private QuartoMapper quartoMapper = Mappers.getMapper(QuartoMapper.class);

    private Quarto quarto;
    private QuartoRequestDTO quartoRequestDTO;

    @BeforeEach
    void setUp() {
        quarto = new Quarto();
        quarto.setId(1L);
        quarto.setNumero(101);
        quarto.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quarto.setTipo(TipoQuarto.UTI);
        quarto.setCapacidade(1);
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        quarto.setPacientes(Collections.singletonList(paciente));

        quartoRequestDTO = new QuartoRequestDTO();
        quartoRequestDTO.setNumero(101);
        quartoRequestDTO.setLocalizacao(LocalizacaoQuarto.SETOR_NORTE);
        quartoRequestDTO.setTipo(TipoQuarto.UTI);
        quartoRequestDTO.setCapacidade(1);
    }

    @Test
    @DisplayName("Deve mapear Quarto para QuartoResponseDTO")
    void shouldMapQuartoToQuartoResponseDTO() {
        QuartoResponseDTO dto = quartoMapper.toResponseDTO(quarto);

        assertNotNull(dto);
        assertEquals(quarto.getId(), dto.getId());
        assertEquals(quarto.getNumero(), dto.getNumero());
        assertEquals(1, dto.getPacientesIds().size());
        assertEquals(1L, dto.getPacientesIds().get(0));
    }

    @Test
    @DisplayName("Deve mapear QuartoRequestDTO para Quarto")
    void shouldMapQuartoRequestDTOToQuarto() {
        Quarto entity = quartoMapper.toEntity(quartoRequestDTO);

        assertNotNull(entity);
        assertEquals(quartoRequestDTO.getNumero(), entity.getNumero());
        assertEquals(quartoRequestDTO.getLocalizacao(), entity.getLocalizacao());
    }

    @Test
    @DisplayName("Deve atualizar Quarto a partir de QuartoRequestDTO")
    void shouldUpdateQuartoFromQuartoRequestDTO() {
        Quarto entity = new Quarto();
        quartoMapper.updateFromDto(quartoRequestDTO, entity);

        assertEquals(quartoRequestDTO.getNumero(), entity.getNumero());
        assertEquals(quartoRequestDTO.getLocalizacao(), entity.getLocalizacao());
    }
}
