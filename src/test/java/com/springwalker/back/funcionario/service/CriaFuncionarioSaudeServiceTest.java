package com.springwalker.back.funcionario.service;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.mapper.FuncionarioSaudeMapper;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.funcionario.enums.Cargo;
import com.springwalker.back.pessoa.enums.Sexo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections; // For empty lists

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriaFuncionarioSaudeServiceTest {

    @Mock
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Mock
    private FuncionarioSaudeMapper funcionarioSaudeMapper;

    @InjectMocks
    private CriaFuncionarioSaudeService criaFuncionarioSaudeService;

    private FuncionarioSaudeRequestDTO requestDTO;
    private FuncionarioSaude funcionarioSaude;
    private FuncionarioSaudeResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        requestDTO = new FuncionarioSaudeRequestDTO();
        requestDTO.setCpf("85055533064");
        requestDTO.setNome("João");
        requestDTO.setEmail("joao.silva@example.com");
        requestDTO.setSexo(Sexo.Masculino);
        requestDTO.setDataNascimento(LocalDate.of(1980, 1, 1));
        requestDTO.setTelefones(Collections.emptyList());
        requestDTO.setCargo(Cargo.MEDICO);
        requestDTO.setEspecialidades(Arrays.asList("Cardiologia"));
        requestDTO.setIdentificacao("CRM123");


        funcionarioSaude = new FuncionarioSaude();
        funcionarioSaude.setId(1L);
        funcionarioSaude.setCpf("85055533064");
        funcionarioSaude.setNome("João");
        funcionarioSaude.setEmail("joao.silva@example.com");
        funcionarioSaude.setIdentificacao("CRM123");


        responseDTO = new FuncionarioSaudeResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCpf("85055533064");
        responseDTO.setNome("João");
        responseDTO.setEmail("joao.silva@example.com");
        responseDTO.setIdentificacao("CRM123");
    }

    @Test
    @DisplayName("Deve criar um novo FuncionarioSaude com sucesso")
    void shouldCreateNewFuncionarioSaudeSuccessfully() {
        when(funcionarioSaudeRepository.existsByCpf(requestDTO.getCpf())).thenReturn(false);
        when(funcionarioSaudeMapper.toEntity(requestDTO)).thenReturn(funcionarioSaude);
        when(funcionarioSaudeRepository.save(funcionarioSaude)).thenReturn(funcionarioSaude);
        when(funcionarioSaudeMapper.toResponseDTO(funcionarioSaude)).thenReturn(responseDTO);

        FuncionarioSaudeResponseDTO result = criaFuncionarioSaudeService.execute(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCpf(), result.getCpf());
        verify(funcionarioSaudeRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(funcionarioSaudeMapper, times(1)).toEntity(requestDTO);
        verify(funcionarioSaudeRepository, times(1)).save(funcionarioSaude);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionarioSaude);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException quando o CPF já existir")
    void shouldThrowIllegalArgumentExceptionWhenCpfAlreadyExists() {
        when(funcionarioSaudeRepository.existsByCpf(requestDTO.getCpf())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                criaFuncionarioSaudeService.execute(requestDTO));

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(funcionarioSaudeRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(funcionarioSaudeMapper, never()).toEntity(any());
        verify(funcionarioSaudeRepository, never()).save(any());
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }
}
