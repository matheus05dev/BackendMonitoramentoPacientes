package com.springwalker.back.paciente.service;

import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.mapper.PacienteMapper;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CriaPacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private QuartoRepository quartoRepository;
    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private CriaPacienteService criaPacienteService;

    private PacienteRequestDTO requestDTO;
    private Paciente paciente;
    private Paciente pacienteSalvo;
    private PacienteResponseDTO responseDTO;
    private Quarto quarto;

    @BeforeEach
    void setUp() {
        requestDTO = new PacienteRequestDTO();
        requestDTO.setCpf("12345678900");
        requestDTO.setNome("Test Patient");
        requestDTO.setQuartoId(1L); // Example quartoId

        paciente = new Paciente();
        paciente.setCpf("12345678900");
        paciente.setNome("Test Patient");

        quarto = new Quarto();
        quarto.setId(1L);
        quarto.setNumero(101); // Corrected: Integer for quarto number

        pacienteSalvo = new Paciente();
        pacienteSalvo.setId(1L);
        pacienteSalvo.setCpf("12345678900");
        pacienteSalvo.setNome("Test Patient");
        pacienteSalvo.setQuarto(quarto);

        responseDTO = new PacienteResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setCpf("12345678900");
        responseDTO.setNome("Test Patient");
        responseDTO.setQuartoId(1L); // Corrected: Use quartoId
    }

    @Test
    @DisplayName("Should create a patient successfully with a valid Quarto ID")
    void shouldCreatePatientSuccessfullyWithQuartoId() {
        when(pacienteRepository.existsByCpf(requestDTO.getCpf())).thenReturn(false);
        when(pacienteMapper.toEntity(requestDTO)).thenReturn(paciente);
        when(quartoRepository.findById(requestDTO.getQuartoId())).thenReturn(Optional.of(quarto));
        when(pacienteRepository.save(paciente)).thenReturn(pacienteSalvo);
        when(pacienteMapper.toResponseDTO(pacienteSalvo)).thenReturn(responseDTO);

        PacienteResponseDTO result = criaPacienteService.execute(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCpf(), result.getCpf());
        assertEquals(responseDTO.getNome(), result.getNome());
        assertEquals(responseDTO.getQuartoId(), result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(pacienteMapper, times(1)).toEntity(requestDTO);
        verify(quartoRepository, times(1)).findById(requestDTO.getQuartoId());
        verify(pacienteRepository, times(1)).save(paciente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteSalvo);
    }

    @Test
    @DisplayName("Should create a patient successfully without a Quarto ID")
    void shouldCreatePatientSuccessfullyWithoutQuartoId() {
        requestDTO.setQuartoId(null);
        pacienteSalvo.setQuarto(null);
        responseDTO.setQuartoId(null); // Corrected: Use quartoId

        when(pacienteRepository.existsByCpf(requestDTO.getCpf())).thenReturn(false);
        when(pacienteMapper.toEntity(requestDTO)).thenReturn(paciente);
        when(pacienteRepository.save(paciente)).thenReturn(pacienteSalvo);
        when(pacienteMapper.toResponseDTO(pacienteSalvo)).thenReturn(responseDTO);

        PacienteResponseDTO result = criaPacienteService.execute(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        assertEquals(responseDTO.getCpf(), result.getCpf());
        assertEquals(responseDTO.getNome(), result.getNome());
        assertNull(result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(pacienteMapper, times(1)).toEntity(requestDTO);
        verify(quartoRepository, never()).findById(anyLong()); // Ensure quartoRepository is not called
        verify(pacienteRepository, times(1)).save(paciente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteSalvo);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when CPF is duplicated")
    void shouldThrowExceptionWhenCpfIsDuplicated() {
        when(pacienteRepository.existsByCpf(requestDTO.getCpf())).thenReturn(true);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            criaPacienteService.execute(requestDTO);
        });

        assertEquals("CPF já cadastrado.", exception.getMessage());
        verify(pacienteRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(pacienteMapper, never()).toEntity(any(PacienteRequestDTO.class));
        verify(quartoRepository, never()).findById(anyLong());
        verify(pacienteRepository, never()).save(any(Paciente.class));
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }

    @Test
    @DisplayName("Should throw RuntimeException when Quarto ID is provided but not found")
    void shouldThrowExceptionWhenQuartoIdNotFound() {
        when(pacienteRepository.existsByCpf(requestDTO.getCpf())).thenReturn(false);
        when(pacienteMapper.toEntity(requestDTO)).thenReturn(paciente);
        when(quartoRepository.findById(requestDTO.getQuartoId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            criaPacienteService.execute(requestDTO);
        });

        assertEquals("Quarto não encontrado com ID: " + requestDTO.getQuartoId(), exception.getMessage());
        verify(pacienteRepository, times(1)).existsByCpf(requestDTO.getCpf());
        verify(pacienteMapper, times(1)).toEntity(requestDTO);
        verify(quartoRepository, times(1)).findById(requestDTO.getQuartoId());
        verify(pacienteRepository, never()).save(any(Paciente.class));
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }
}
