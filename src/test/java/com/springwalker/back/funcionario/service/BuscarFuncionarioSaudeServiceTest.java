package com.springwalker.back.funcionario.service;

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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarFuncionarioSaudeServiceTest {

    @Mock
    private FuncionarioSaudeRepository funcionarioSaudeRepository;

    @Mock
    private FuncionarioSaudeMapper funcionarioSaudeMapper;

    @InjectMocks
    private BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;

    private FuncionarioSaude funcionario1;
    private FuncionarioSaude funcionario2;
    private FuncionarioSaudeResponseDTO responseDTO1;
    private FuncionarioSaudeResponseDTO responseDTO2;

    @BeforeEach
    void setUp() {
        // Instanciando FuncionarioSaude usando o builder
        funcionario1 = FuncionarioSaude.builder()
                .id(1L)
                .cpf("85055533064")
                .nome("João Silva")
                .email("joao@example.com")
                .sexo(Sexo.Masculino)
                .dataNascimento(LocalDate.of(1980, 1, 1))
                .telefones(new ArrayList<>())
                .cargo(Cargo.MEDICO)
                .identificacao("CRM123")
                .especialidades(new ArrayList<>())
                .atendimentos(new ArrayList<>())
                .atendimentosComplicacao(new ArrayList<>())
                .build();

        funcionario2 = FuncionarioSaude.builder()
                .id(2L)
                .cpf("55566677788")
                .nome("Maria Souza")
                .email("maria@example.com")
                .sexo(Sexo.Feminino)
                .dataNascimento(LocalDate.of(1990, 2, 2))
                .telefones(new ArrayList<>())
                .cargo(Cargo.ENFERMEIRO) // Assumindo que "Enfermeira" mapeia para ENFERMEIRO
                .identificacao("COREN456")
                .especialidades(new ArrayList<>())
                .atendimentos(new ArrayList<>())
                .atendimentosComplicacao(new ArrayList<>())
                .build();

        // Instanciando FuncionarioSaudeResponseDTO usando setters
        responseDTO1 = new FuncionarioSaudeResponseDTO();
        responseDTO1.setId(1L);
        responseDTO1.setCpf("85055533064");
        responseDTO1.setNome("João Silva");
        responseDTO1.setEmail("joao@example.com");
        responseDTO1.setSexo(Sexo.Masculino);
        responseDTO1.setDataNascimento(LocalDate.of(1980, 1, 1));
        responseDTO1.setTelefones(new ArrayList<>());
        responseDTO1.setCargo(Cargo.MEDICO);
        responseDTO1.setIdentificacao("CRM123");
        responseDTO1.setEspecialidades(new ArrayList<>());
        responseDTO1.setId(1L);
        responseDTO1.setAtendimentosIds(new ArrayList<>());
        responseDTO1.setAtendimentosComplicacaoIds(new ArrayList<>());


        responseDTO2 = new FuncionarioSaudeResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCpf("55566677788");
        responseDTO2.setNome("Maria Souza");
        responseDTO2.setEmail("maria@example.com");
        responseDTO2.setSexo(Sexo.Feminino);
        responseDTO2.setDataNascimento(LocalDate.of(1990, 2, 2));
        responseDTO2.setTelefones(new ArrayList<>());
        responseDTO2.setCargo(Cargo.ENFERMEIRO);
        responseDTO2.setIdentificacao("COREN456");
        responseDTO2.setEspecialidades(new ArrayList<>());
        responseDTO2.setAtendimentosIds(new ArrayList<>());
        responseDTO2.setAtendimentosComplicacaoIds(new ArrayList<>());
    }

    @Test
    @DisplayName("Deve listar todos os FuncionarioSaude com sucesso")
    void shouldListAllFuncionarioSaudeSuccessfully() {
        when(funcionarioSaudeRepository.findAll()).thenReturn(Arrays.asList(funcionario1, funcionario2));
        when(funcionarioSaudeMapper.toResponseDTO(funcionario1)).thenReturn(responseDTO1);
        when(funcionarioSaudeMapper.toResponseDTO(funcionario2)).thenReturn(responseDTO2);

        List<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(responseDTO1.getId(), result.get(0).getId());
        assertEquals(responseDTO2.getId(), result.get(1).getId());
        verify(funcionarioSaudeRepository, times(1)).findAll();
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionario1);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionario2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum FuncionarioSaude existir")
    void shouldReturnEmptyListWhenNoFuncionarioSaudeExist() {
        when(funcionarioSaudeRepository.findAll()).thenReturn(Collections.emptyList());

        List<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.listarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(funcionarioSaudeRepository, times(1)).findAll();
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve encontrar FuncionarioSaude por ID com sucesso")
    void shouldFindFuncionarioSaudeByIdSuccessfully() {
        when(funcionarioSaudeRepository.findById(1L)).thenReturn(Optional.of(funcionario1));
        when(funcionarioSaudeMapper.toResponseDTO(funcionario1)).thenReturn(responseDTO1);

        Optional<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(responseDTO1.getId(), result.get().getId());
        verify(funcionarioSaudeRepository, times(1)).findById(1L);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionario1);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando FuncionarioSaude não for encontrado por ID")
    void shouldReturnEmptyOptionalWhenFuncionarioSaudeNotFoundById() {
        when(funcionarioSaudeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorId(99L);

        assertFalse(result.isPresent());
        verify(funcionarioSaudeRepository, times(1)).findById(99L);
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve encontrar FuncionarioSaude por nome com sucesso")
    void shouldFindFuncionarioSaudeByNameSuccessfully() {
        String name = "João Silva";
        when(funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(name)).thenReturn(Collections.singletonList(funcionario1));
        when(funcionarioSaudeMapper.toResponseDTO(funcionario1)).thenReturn(responseDTO1);

        List<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorNome(name);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTO1.getNome(), result.get(0).getNome());
        verify(funcionarioSaudeRepository, times(1)).findFuncionarioSaudesByNomeContaining(name);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionario1);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando FuncionarioSaude não for encontrado por nome")
    void shouldReturnEmptyListWhenFuncionarioSaudeNotFoundByName() {
        String name = "Inexistente";
        when(funcionarioSaudeRepository.findFuncionarioSaudesByNomeContaining(name)).thenReturn(Collections.emptyList());

        List<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorNome(name);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(funcionarioSaudeRepository, times(1)).findFuncionarioSaudesByNomeContaining(name);
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }

    @Test
    @DisplayName("Deve encontrar FuncionarioSaude por CPF com sucesso")
    void shouldFindFuncionarioSaudeByCpfSuccessfully() {
        String cpf = "85055533064";
        when(funcionarioSaudeRepository.findFuncionarioSaudeByCpf(cpf)).thenReturn(Optional.of(funcionario1));
        when(funcionarioSaudeMapper.toResponseDTO(funcionario1)).thenReturn(responseDTO1);

        Optional<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorCpf(cpf);

        assertTrue(result.isPresent());
        assertEquals(responseDTO1.getCpf(), result.get().getCpf());
        verify(funcionarioSaudeRepository, times(1)).findFuncionarioSaudeByCpf(cpf);
        verify(funcionarioSaudeMapper, times(1)).toResponseDTO(funcionario1);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando FuncionarioSaude não for encontrado por CPF")
    void shouldReturnEmptyOptionalWhenFuncionarioSaudeNotFoundByCpf() {
        String cpf = "99988877766";
        when(funcionarioSaudeRepository.findFuncionarioSaudeByCpf(cpf)).thenReturn(Optional.empty());

        Optional<FuncionarioSaudeResponseDTO> result = buscarFuncionarioSaudeService.buscarPorCpf(cpf);

        assertFalse(result.isPresent());
        verify(funcionarioSaudeRepository, times(1)).findFuncionarioSaudeByCpf(cpf);
        verify(funcionarioSaudeMapper, never()).toResponseDTO(any());
    }
}
