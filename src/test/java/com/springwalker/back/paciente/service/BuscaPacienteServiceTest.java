package com.springwalker.back.paciente.service;

import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.mapper.PacienteMapper;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscaPacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private BuscaPacienteService buscaPacienteService;

    private Paciente paciente1;
    private Paciente paciente2;
    private PacienteResponseDTO responseDTO1;
    private PacienteResponseDTO responseDTO2;

    @BeforeEach
    void setUp() {
        paciente1 = new Paciente();
        paciente1.setId(1L);
        paciente1.setCpf("11122233344");
        paciente1.setNome("Paciente Um");

        paciente2 = new Paciente();
        paciente2.setId(2L);
        paciente2.setCpf("55566677788");
        paciente2.setNome("Paciente Dois");

        responseDTO1 = new PacienteResponseDTO();
        responseDTO1.setId(1L);
        responseDTO1.setCpf("11122233344");
        responseDTO1.setNome("Paciente Um");

        responseDTO2 = new PacienteResponseDTO();
        responseDTO2.setId(2L);
        responseDTO2.setCpf("55566677788");
        responseDTO2.setNome("Paciente Dois");
    }

    @Test
    @DisplayName("Deve listar todos os pacientes quando houver pacientes")
    void shouldListAllPatientsWhenPatientsExist() {
        List<Paciente> pacientes = Arrays.asList(paciente1, paciente2);
        List<PacienteResponseDTO> responseDTOs = Arrays.asList(responseDTO1, responseDTO2);

        when(pacienteRepository.findAll()).thenReturn(pacientes);
        when(pacienteMapper.toResponseDTO(paciente1)).thenReturn(responseDTO1);
        when(pacienteMapper.toResponseDTO(paciente2)).thenReturn(responseDTO2);

        List<PacienteResponseDTO> result = buscaPacienteService.listarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(responseDTOs, result);
        verify(pacienteRepository, times(1)).findAll();
        verify(pacienteMapper, times(1)).toResponseDTO(paciente1);
        verify(pacienteMapper, times(1)).toResponseDTO(paciente2);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia quando não houver pacientes")
    void shouldReturnEmptyListWhenNoPatientsExist() {
        when(pacienteRepository.findAll()).thenReturn(Collections.emptyList());

        List<PacienteResponseDTO> result = buscaPacienteService.listarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pacienteRepository, times(1)).findAll();
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve encontrar paciente por ID quando o paciente existir")
    void shouldFindPatientByIdWhenPatientExists() {
        when(pacienteRepository.findById(1L)).thenReturn(Optional.of(paciente1));
        when(pacienteMapper.toResponseDTO(paciente1)).thenReturn(responseDTO1);

        Optional<PacienteResponseDTO> result = buscaPacienteService.buscarPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(responseDTO1, result.get());
        verify(pacienteRepository, times(1)).findById(1L);
        verify(pacienteMapper, times(1)).toResponseDTO(paciente1);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o paciente por ID não existir")
    void shouldReturnEmptyWhenPatientByIdDoesNotExist() {
        when(pacienteRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<PacienteResponseDTO> result = buscaPacienteService.buscarPorId(99L);

        assertFalse(result.isPresent());
        verify(pacienteRepository, times(1)).findById(99L);
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve encontrar pacientes por nome quando os pacientes existirem")
    void shouldFindPatientsByNameWhenPatientsExist() {
        List<Paciente> pacientes = Collections.singletonList(paciente1);
        List<PacienteResponseDTO> responseDTOs = Collections.singletonList(responseDTO1);

        when(pacienteRepository.findPacientesByNomeContaining("Um")).thenReturn(pacientes);
        when(pacienteMapper.toResponseDTO(paciente1)).thenReturn(responseDTO1);

        List<PacienteResponseDTO> result = buscaPacienteService.buscarPorNome("Um");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(responseDTOs, result);
        verify(pacienteRepository, times(1)).findPacientesByNomeContaining("Um");
        verify(pacienteMapper, times(1)).toResponseDTO(paciente1);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando nenhum paciente for encontrado por nome")
    void shouldReturnEmptyListWhenNoPatientsFoundByName() {
        when(pacienteRepository.findPacientesByNomeContaining("NonExistent")).thenReturn(Collections.emptyList());

        List<PacienteResponseDTO> result = buscaPacienteService.buscarPorNome("NonExistent");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pacienteRepository, times(1)).findPacientesByNomeContaining("NonExistent");
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve encontrar paciente por CPF quando o paciente existir")
    void shouldFindPatientByCpfWhenPatientExists() {
        when(pacienteRepository.findByCpf("11122233344")).thenReturn(Optional.of(paciente1));
        when(pacienteMapper.toResponseDTO(paciente1)).thenReturn(responseDTO1);

        Optional<PacienteResponseDTO> result = buscaPacienteService.buscarPorCpf("11122233344");

        assertTrue(result.isPresent());
        assertEquals(responseDTO1, result.get());
        verify(pacienteRepository, times(1)).findByCpf("11122233344");
        verify(pacienteMapper, times(1)).toResponseDTO(paciente1);
    }

    @Test
    @DisplayName("Deve retornar vazio quando o paciente por CPF não existir")
    void shouldReturnEmptyWhenPatientByCpfDoesNotExist() {
        when(pacienteRepository.findByCpf("99988877766")).thenReturn(Optional.empty());

        Optional<PacienteResponseDTO> result = buscaPacienteService.buscarPorCpf("99988877766");

        assertFalse(result.isPresent());
        verify(pacienteRepository, times(1)).findByCpf("99988877766");
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }
}
