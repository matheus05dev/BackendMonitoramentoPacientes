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
class AlteraPacienteServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;
    @Mock
    private QuartoRepository quartoRepository;
    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private AlteraPacienteService alteraPacienteService;

    private Long pacienteId;
    private PacienteRequestDTO requestDTO;
    private Paciente pacienteExistente;
    private Paciente pacienteAtualizado;
    private PacienteResponseDTO responseDTO;
    private Quarto quartoExistente;
    private Quarto novoQuarto;

    @BeforeEach
    void setUp() {
        pacienteId = 1L;

        quartoExistente = new Quarto();
        quartoExistente.setId(10L);
        quartoExistente.setNumero(101);

        novoQuarto = new Quarto();
        novoQuarto.setId(20L);
        novoQuarto.setNumero(202);
        pacienteExistente = new Paciente();
        pacienteExistente.setId(pacienteId);
        pacienteExistente.setCpf("11122233344");
        pacienteExistente.setNome("Paciente Antigo");
        pacienteExistente.setQuarto(quartoExistente);

        requestDTO = new PacienteRequestDTO();
        requestDTO.setCpf("11122233344");
        requestDTO.setNome("Paciente Novo Nome");
        requestDTO.setQuartoId(novoQuarto.getId());

        pacienteAtualizado = new Paciente();
        pacienteAtualizado.setId(pacienteId);
        pacienteAtualizado.setCpf("11122233344");
        pacienteAtualizado.setNome("Paciente Novo Nome");
        pacienteAtualizado.setQuarto(novoQuarto);

        responseDTO = new PacienteResponseDTO();
        responseDTO.setId(pacienteId);
        responseDTO.setCpf("11122233344");
        responseDTO.setNome("Paciente Novo Nome");
        responseDTO.setQuartoId(novoQuarto.getId());
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso com mudança de ID de Quarto")
    void shouldUpdatePatientSuccessfullyWithQuartoIdChange() {
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        doNothing().when(pacienteMapper).updateFromDto(requestDTO, pacienteExistente);
        when(quartoRepository.findById(requestDTO.getQuartoId())).thenReturn(Optional.of(novoQuarto));
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteAtualizado);
        when(pacienteMapper.toResponseDTO(pacienteAtualizado)).thenReturn(responseDTO);

        PacienteResponseDTO result = alteraPacienteService.execute(pacienteId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getNome(), result.getNome());
        assertEquals(responseDTO.getQuartoId(), result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, times(1)).updateFromDto(requestDTO, pacienteExistente);
        verify(quartoRepository, times(1)).findById(requestDTO.getQuartoId());
        verify(pacienteRepository, times(1)).save(pacienteExistente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteAtualizado);
        assertEquals(novoQuarto, pacienteExistente.getQuarto()); // Verify quarto was set
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso sem mudança de ID de Quarto (mesmo ID)")
    void shouldUpdatePatientSuccessfullyWithoutQuartoIdChange() {
        requestDTO.setQuartoId(quartoExistente.getId()); // Set to existing quarto ID
        pacienteAtualizado.setQuarto(quartoExistente);
        responseDTO.setQuartoId(quartoExistente.getId()); // Corrected: Use quartoId

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        doNothing().when(pacienteMapper).updateFromDto(requestDTO, pacienteExistente);
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteAtualizado);
        when(pacienteMapper.toResponseDTO(pacienteAtualizado)).thenReturn(responseDTO);

        PacienteResponseDTO result = alteraPacienteService.execute(pacienteId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getNome(), result.getNome());
        assertEquals(responseDTO.getQuartoId(), result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, times(1)).updateFromDto(requestDTO, pacienteExistente);
        verify(quartoRepository, never()).findById(anyLong()); // QuartoRepository should not be called
        verify(pacienteRepository, times(1)).save(pacienteExistente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteAtualizado);
        assertEquals(quartoExistente, pacienteExistente.getQuarto()); // Quarto should remain the same
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso removendo associação de Quarto")
    void shouldUpdatePatientSuccessfullyRemovingQuartoAssociation() {
        requestDTO.setQuartoId(null); // Remove quarto association
        pacienteAtualizado.setQuarto(null);
        responseDTO.setQuartoId(null); // Corrected: Use quartoId

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        doNothing().when(pacienteMapper).updateFromDto(requestDTO, pacienteExistente);
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteAtualizado);
        when(pacienteMapper.toResponseDTO(pacienteAtualizado)).thenReturn(responseDTO);

        PacienteResponseDTO result = alteraPacienteService.execute(pacienteId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getNome(), result.getNome());
        assertNull(result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, times(1)).updateFromDto(requestDTO, pacienteExistente);
        verify(quartoRepository, never()).findById(anyLong()); // QuartoRepository should not be called
        verify(pacienteRepository, times(1)).save(pacienteExistente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteAtualizado);
        assertNull(pacienteExistente.getQuarto()); // Quarto should be null
    }

    @Test
    @DisplayName("Deve atualizar paciente com sucesso quando paciente já não tem Quarto e requestDTO.quartoId é nulo")
    void shouldUpdatePatientSuccessfullyWhenNoQuartoAndRequestQuartoIdIsNull() {
        pacienteExistente.setQuarto(null); // Patient initially has no quarto
        requestDTO.setQuartoId(null); // Request also has no quarto
        pacienteAtualizado.setQuarto(null);
        responseDTO.setQuartoId(null); // Corrected: Use quartoId

        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        doNothing().when(pacienteMapper).updateFromDto(requestDTO, pacienteExistente);
        when(pacienteRepository.save(pacienteExistente)).thenReturn(pacienteAtualizado);
        when(pacienteMapper.toResponseDTO(pacienteAtualizado)).thenReturn(responseDTO);

        PacienteResponseDTO result = alteraPacienteService.execute(pacienteId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getNome(), result.getNome());
        assertNull(result.getQuartoId()); // Corrected: Check quartoId

        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, times(1)).updateFromDto(requestDTO, pacienteExistente);
        verify(quartoRepository, never()).findById(anyLong()); // QuartoRepository should not be called
        verify(pacienteRepository, times(1)).save(pacienteExistente);
        verify(pacienteMapper, times(1)).toResponseDTO(pacienteAtualizado);
        assertNull(pacienteExistente.getQuarto()); // Quarto should remain null
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando paciente não for encontrado")
    void shouldThrowExceptionWhenPatientNotFound() {
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alteraPacienteService.execute(pacienteId, requestDTO);
        });

        assertEquals("Paciente não encontrado com ID: " + pacienteId, exception.getMessage());
        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, never()).updateFromDto(any(PacienteRequestDTO.class), any(Paciente.class));
        verify(quartoRepository, never()).findById(anyLong());
        verify(pacienteRepository, never()).save(any(Paciente.class));
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando novo ID de Quarto for fornecido mas não encontrado")
    void shouldThrowExceptionWhenNewQuartoIdNotFound() {
        when(pacienteRepository.findById(pacienteId)).thenReturn(Optional.of(pacienteExistente));
        doNothing().when(pacienteMapper).updateFromDto(requestDTO, pacienteExistente);
        when(quartoRepository.findById(requestDTO.getQuartoId())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            alteraPacienteService.execute(pacienteId, requestDTO);
        });

        assertEquals("Quarto não encontrado com ID: " + requestDTO.getQuartoId(), exception.getMessage());
        verify(pacienteRepository, times(1)).findById(pacienteId);
        verify(pacienteMapper, times(1)).updateFromDto(requestDTO, pacienteExistente);
        verify(quartoRepository, times(1)).findById(requestDTO.getQuartoId());
        verify(pacienteRepository, never()).save(any(Paciente.class));
        verify(pacienteMapper, never()).toResponseDTO(any(Paciente.class));
    }
}
