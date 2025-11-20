package com.springwalker.back.funcionario.mapper;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.pessoa.mapper.TelefoneMapper;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.pessoa.enums.Sexo;
import com.springwalker.back.pessoa.model.Telefone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

class FuncionarioSaudeMapperTest {

    @InjectMocks
    private FuncionarioSaudeMapper funcionarioSaudeMapper = Mappers.getMapper(FuncionarioSaudeMapper.class);

    @Mock
    private TelefoneMapper telefoneMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private FuncionarioSaudeRequestDTO createRequestDTO() {
        FuncionarioSaudeRequestDTO dto = new FuncionarioSaudeRequestDTO();
        dto.setNome("Funcionario Teste");
        dto.setEmail("teste@example.com");
        dto.setCpf("123.456.789-00");
        dto.setDataNascimento(LocalDate.of(1990, 1, 1));
        dto.setSexo(Sexo.Masculino);
        dto.setCargo(Cargo.MEDICO);
        dto.setEspecialidades(new ArrayList<>(Arrays.asList("Cardiologia", "Pediatria"))); // Changed to mutable ArrayList
        dto.setIdentificacao("CRM123");
        dto.setTelefones(new ArrayList<>());
        return dto;
    }

    private FuncionarioSaude createEntity() {
        FuncionarioSaude entity = new FuncionarioSaude();
        entity.setId(1L);
        entity.setNome("Funcionario Teste");
        entity.setEmail("teste@example.com");
        entity.setCpf("123.456.789-00");
        entity.setDataNascimento(LocalDate.of(1990, 1, 1));
        entity.setSexo(Sexo.Masculino);
        entity.setCargo(Cargo.MEDICO);
        entity.setEspecialidades(new ArrayList<>(Arrays.asList("Cardiologia", "Pediatria"))); // Changed to mutable ArrayList
        entity.setIdentificacao("CRM123");
        entity.setTelefones(new ArrayList<>());
        entity.setAtendimentos(new ArrayList<>(Arrays.asList(Atendimento.builder().id(10L).build(), Atendimento.builder().id(11L).build()))); // Changed to mutable ArrayList
        entity.setAtendimentosComplicacao(new ArrayList<>(Arrays.asList(Atendimento.builder().id(20L).build()))); // Changed to mutable ArrayList
        return entity;
    }

    @Test
    @DisplayName("Deve mapear FuncionarioSaudeRequestDTO para FuncionarioSaude entity")
    void toEntity_shouldMapDtoToEntity() {
        FuncionarioSaudeRequestDTO dto = createRequestDTO();
        when(telefoneMapper.toEntity(anyList())).thenReturn(new ArrayList<>());

        FuncionarioSaude entity = funcionarioSaudeMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getNome(), entity.getNome());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getCpf(), entity.getCpf());
        assertEquals(dto.getDataNascimento(), entity.getDataNascimento());
        assertEquals(dto.getSexo(), entity.getSexo());
        assertEquals(dto.getCargo(), entity.getCargo());
        assertEquals(dto.getEspecialidades(), entity.getEspecialidades());
        assertEquals(dto.getIdentificacao(), entity.getIdentificacao());
        assertNotNull(entity.getTelefones());
        assertTrue(entity.getTelefones().isEmpty());
    }

    @Test
    @DisplayName("Deve mapear FuncionarioSaude entity para FuncionarioSaudeResponseDTO")
    void toResponseDTO_shouldMapEntityToDto() {
        FuncionarioSaude entity = createEntity();
        when(telefoneMapper.toDTO(anyList())).thenReturn(new ArrayList<>());

        FuncionarioSaudeResponseDTO dto = funcionarioSaudeMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getNome(), dto.getNome());
        assertEquals(entity.getEmail(), dto.getEmail());
        assertEquals(entity.getCpf(), dto.getCpf());
        assertEquals(entity.getDataNascimento(), dto.getDataNascimento());
        assertEquals(entity.getSexo(), dto.getSexo());
        assertEquals(entity.getCargo(), dto.getCargo());
        assertEquals(entity.getEspecialidades(), dto.getEspecialidades());
        assertEquals(entity.getIdentificacao(), dto.getIdentificacao());
        assertNotNull(dto.getTelefones());
        assertTrue(dto.getTelefones().isEmpty());
        assertNotNull(dto.getAtendimentosIds());
        assertEquals(2, dto.getAtendimentosIds().size());
        assertTrue(dto.getAtendimentosIds().contains(10L));
        assertTrue(dto.getAtendimentosIds().contains(11L));
        assertNotNull(dto.getAtendimentosComplicacaoIds());
        assertEquals(1, dto.getAtendimentosComplicacaoIds().size());
        assertTrue(dto.getAtendimentosComplicacaoIds().contains(20L));
    }

    @Test
    @DisplayName("Deve atualizar FuncionarioSaude entity a partir de FuncionarioSaudeRequestDTO")
    void updateFromDto_shouldUpdateEntityFromDto() {
        FuncionarioSaudeRequestDTO dto = new FuncionarioSaudeRequestDTO();
        dto.setNome("Nome Atualizado");
        dto.setCargo(Cargo.ENFERMEIRO);
        dto.setEspecialidades(new ArrayList<>(Collections.singletonList("Enfermagem"))); // Changed to mutable ArrayList
        dto.setIdentificacao("COREN456");

        FuncionarioSaude entity = createEntity();
        Long originalId = entity.getId();
        String originalCpf = entity.getCpf();
        String originalEmail = entity.getEmail();

        funcionarioSaudeMapper.updateFromDto(dto, entity);

        assertEquals(originalId, entity.getId()); // ID não deve ser alterado
        assertEquals(originalCpf, entity.getCpf()); // CPF não deve ser alterado
        assertEquals(originalEmail, entity.getEmail()); // Email não deve ser alterado se não estiver no DTO

        assertEquals(dto.getNome(), entity.getNome());
        assertEquals(dto.getCargo(), entity.getCargo());
        assertEquals(dto.getEspecialidades(), entity.getEspecialidades());
        assertEquals(dto.getIdentificacao(), entity.getIdentificacao());
    }

    @Test
    @DisplayName("Deve lidar com listas de atendimentos nulas ou vazias no mapeamento para IDs")
    void mapAtendimentosToIds_shouldHandleNullOrEmptyLists() {
        List<Long> resultNull = funcionarioSaudeMapper.mapAtendimentosToIds(null);
        assertNotNull(resultNull);
        assertTrue(resultNull.isEmpty());

        List<Long> resultEmpty = funcionarioSaudeMapper.mapAtendimentosToIds(Collections.emptyList());
        assertNotNull(resultEmpty);
        assertTrue(resultEmpty.isEmpty());

        List<Atendimento> atendimentos = Arrays.asList(Atendimento.builder().id(1L).build(), Atendimento.builder().id(2L).build());
        List<Long> result = funcionarioSaudeMapper.mapAtendimentosToIds(atendimentos);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(1L));
        assertTrue(result.contains(2L));
    }
}