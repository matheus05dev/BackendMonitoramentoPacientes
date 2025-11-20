package com.springwalker.back.paciente.mapper;

import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.pessoa.enums.Sexo;
import com.springwalker.back.pessoa.mapper.TelefoneMapper;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PacienteMapperTest {

    @InjectMocks
    private PacienteMapper pacienteMapper = Mappers.getMapper(PacienteMapper.class);

    @Mock
    private TelefoneMapper telefoneMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private PacienteRequestDTO createPacienteRequestDTO() {
        PacienteRequestDTO requestDTO = new PacienteRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setEmail("joao.silva@example.com");
        requestDTO.setSexo(Sexo.Masculino);
        requestDTO.setDataNascimento(LocalDate.of(1990, 1, 1));
        requestDTO.setCpf("690.438.980-07");
        requestDTO.setTelefones(new ArrayList<>()); // Changed to mutable list
        requestDTO.setAlergias(new ArrayList<>(List.of("Alergia a amendoim"))); // Changed to mutable list
        requestDTO.setQuartoId(1L);
        return requestDTO;
    }

    private Paciente createPacienteEntity() {
        Paciente paciente = new Paciente();
        paciente.setId(1L);
        paciente.setNome("João Silva");
        paciente.setEmail("joao.silva@example.com");
        paciente.setSexo(Sexo.Masculino);
        paciente.setDataNascimento(LocalDate.of(1990, 1, 1));
        paciente.setCpf("690.438.980-07");
        paciente.setTelefones(new ArrayList<>()); // Changed to mutable list
        paciente.setAlergias(new ArrayList<>(List.of("Alergia a amendoim"))); // Changed to mutable list
        Quarto quarto = new Quarto();
        quarto.setId(1L);
        paciente.setQuarto(quarto);
        return paciente;
    }

    @Test
    @DisplayName("Deve mapear PacienteRequestDTO para Paciente entity")
    void toEntity_shouldMapDtoToEntity() {
        PacienteRequestDTO dto = createPacienteRequestDTO();
        when(telefoneMapper.toEntity(dto.getTelefones())).thenReturn(new ArrayList<>()); // Changed to mutable list

        Paciente entity = pacienteMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getNome(), entity.getNome());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getSexo(), entity.getSexo());
        assertEquals(dto.getDataNascimento(), entity.getDataNascimento());
        assertEquals(dto.getCpf(), entity.getCpf());
        assertEquals(dto.getAlergias(), entity.getAlergias());
        assertNull(entity.getQuarto()); // Quarto é ignorado no mapeamento inicial
    }

    @Test
    @DisplayName("Deve mapear Paciente entity para PacienteResponseDTO")
    void toResponseDTO_shouldMapEntityToDto() {
        Paciente entity = createPacienteEntity();
        when(telefoneMapper.toDTO(entity.getTelefones())).thenReturn(new ArrayList<>()); // Changed to mutable list

        PacienteResponseDTO dto = pacienteMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNome(), dto.getNome());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getSexo(), dto.getSexo());
        assertEquals(entity.getDataNascimento(), dto.getDataNascimento());
        assertEquals(entity.getCpf(), dto.getCpf());
        assertEquals(entity.getAlergias(), dto.getAlergias());
        assertEquals(entity.getQuarto().getId(), dto.getQuartoId());
    }

    @Test
    @DisplayName("Deve atualizar Paciente entity a partir de PacienteRequestDTO")
    void updateFromDto_shouldUpdateEntityFromDto() {
        PacienteRequestDTO dto = createPacienteRequestDTO();
        dto.setNome("João Alterado");
        dto.setEmail("joao.alterado@example.com");
        dto.setQuartoId(2L); // Novo ID de quarto

        Paciente entity = createPacienteEntity(); // Paciente existente
        Long originalId = entity.getId();
        String originalCpf = entity.getCpf();

        when(telefoneMapper.toEntity(dto.getTelefones())).thenReturn(new ArrayList<>()); // Changed to mutable list

        pacienteMapper.updateFromDto(dto, entity);

        assertEquals(originalId, entity.getId()); // ID não deve ser alterado
        assertEquals("João Alterado", entity.getNome());
        assertEquals("joao.alterado@example.com", entity.getEmail());
        assertEquals(originalCpf, entity.getCpf()); // CPF não deve ser alterado pelo updateFromDto
        assertNotNull(entity.getQuarto()); // Quarto é ignorado no updateFromDto, então deve manter o valor original
    }

    @Test
    @DisplayName("Deve lidar com listas de telefones nulas no mapeamento para entidade")
    void toEntity_shouldHandleNullTelefonesList() {
        PacienteRequestDTO dto = createPacienteRequestDTO();
        dto.setTelefones(null);

        when(telefoneMapper.toEntity((List<com.springwalker.back.pessoa.dto.TelefoneDTO>) null)).thenReturn(null);

        Paciente entity = pacienteMapper.toEntity(dto);
        assertNotNull(entity);
        assertNull(entity.getTelefones());
    }

    @Test
    @DisplayName("Deve lidar com listas de telefones nulas no mapeamento para DTO de resposta")
    void toResponseDTO_shouldHandleNullTelefonesList() {
        Paciente entity = createPacienteEntity();
        entity.setTelefones(null);

        when(telefoneMapper.toDTO((List<com.springwalker.back.pessoa.model.Telefone>) null)).thenReturn(null);

        PacienteResponseDTO dto = pacienteMapper.toResponseDTO(entity);
        assertNotNull(dto);
        assertNull(dto.getTelefones());
    }
}
