package com.springwalker.back.funcionario.service;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.funcionario.mapper.FuncionarioSaudeMapper;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.pessoa.enums.Sexo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlteraFuncionarioSaudeServiceTest {

    @Mock
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Mock
    private FuncionarioSaudeMapper funcionarioSaudeMapper;

    @InjectMocks
    private AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;

    private Long funcionarioId;
    private FuncionarioSaudeRequestDTO requestDTO;
    private FuncionarioSaude existingFuncionario;
    private FuncionarioSaude updatedFuncionario;
    private FuncionarioSaudeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        funcionarioId = 1L;

        // Instanciando FuncionarioSaudeRequestDTO usando setters
        requestDTO = new FuncionarioSaudeRequestDTO();
        requestDTO.setNome("João Silva");
        requestDTO.setEmail("joao.silva@example.com");
        requestDTO.setSexo(Sexo.Masculino);
        requestDTO.setDataNascimento(LocalDate.of(1980, 1, 1));
        requestDTO.setCpf("85055533064");
        requestDTO.setTelefones(new ArrayList<>());
        requestDTO.setCargo(Cargo.MEDICO);
        requestDTO.setEspecialidades(Collections.singletonList("Cardiologia"));
        requestDTO.setIdentificacao("CRM123");

        // Instanciando FuncionarioSaude usando o builder
        existingFuncionario = FuncionarioSaude.builder()
                .id(funcionarioId)
                .cpf("11122233344")
                .nome("Maria Souza")
                .email("maria.souza@example.com")
                .sexo(Sexo.Feminino)
                .dataNascimento(LocalDate.of(1990, 2, 2))
                .telefones(new ArrayList<>())
                .cargo(Cargo.ENFERMEIRO)
                .identificacao("COREN456")
                .especialidades(new ArrayList<>())
                .atendimentos(new ArrayList<>())
                .atendimentosComplicacao(new ArrayList<>())
                .build();

        updatedFuncionario = FuncionarioSaude.builder()
                .id(funcionarioId)
                .cpf(requestDTO.getCpf())
                .nome(requestDTO.getNome())
                .email(requestDTO.getEmail())
                .sexo(requestDTO.getSexo())
                .dataNascimento(requestDTO.getDataNascimento())
                .telefones(new ArrayList<>())
                .cargo(requestDTO.getCargo())
                .identificacao(requestDTO.getIdentificacao())
                .especialidades(requestDTO.getEspecialidades())
                .atendimentos(new ArrayList<>())
                .atendimentosComplicacao(new ArrayList<>())
                .build();

        // Instanciando FuncionarioSaudeResponseDTO usando setters
        responseDTO = new FuncionarioSaudeResponseDTO();
        responseDTO.setId(funcionarioId);
        responseDTO.setCpf(requestDTO.getCpf());
        responseDTO.setNome(requestDTO.getNome());
        responseDTO.setEmail(requestDTO.getEmail());
        responseDTO.setSexo(requestDTO.getSexo());
        responseDTO.setDataNascimento(requestDTO.getDataNascimento());
        responseDTO.setTelefones(requestDTO.getTelefones());
        responseDTO.setCargo(requestDTO.getCargo());
        responseDTO.setIdentificacao(requestDTO.getIdentificacao());
        responseDTO.setEspecialidades(requestDTO.getEspecialidades());
        responseDTO.setAtendimentosIds(new ArrayList<>());
        responseDTO.setAtendimentosComplicacaoIds(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve alterar um FuncionarioSaude existente com sucesso")
    void shouldAlterExistingFuncionarioSaudeSuccessfully() {
        when(funcionarioSaudeRepository.findById(funcionarioId)).thenReturn(Optional.of(existingFuncionario));
        doNothing().when(funcionarioSaudeMapper).updateFromDto(requestDTO, existingFuncionario);
        when(funcionarioSaudeRepository.save(existingFuncionario)).thenReturn(updatedFuncionario);
        when(funcionarioSaudeMapper.toResponseDTO(updatedFuncionario)).thenReturn(responseDTO);

        FuncionarioSaudeResponseDTO result = alteraFuncionarioSaudeService.execute(funcionarioId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCpf(), result.getCpf());
        verify(funcionarioSaudeRepository, times(1)).findById(funcionarioId);
        verify(funcionarioSaudeMapper, times(1)).updateFromDto(requestDTO, existingFuncionario);
        verify(funcionarioSaudeRepository, times(1)).save(existingFuncionario);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(updatedFuncionario);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando FuncionarioSaude não for encontrado")
    void shouldThrowRuntimeExceptionWhenFuncionarioSaudeNotFound() {
        when(funcionarioSaudeRepository.findById(funcionarioId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                alteraFuncionarioSaudeService.execute(funcionarioId, requestDTO));

        assertEquals("Funcionário não encontrado com ID: " + funcionarioId, exception.getMessage());
        verify(funcionarioSaudeRepository, times(1)).findById(funcionarioId);
        verify(funcionarioSaudeMapper, never()).updateFromDto(any(), any());
        verify(funcionarioSaudeRepository, never()).save(any());
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }
}
