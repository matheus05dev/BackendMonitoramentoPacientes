package com.springwalker.back.monitoramento.service.leitura;

import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para BuscaLeituraService")
class BuscaLeituraServiceTest {

    @Mock
    private LeituraSensorRepository leituraSensorRepository;
    @Mock
    private LeituraSensorMapper leituraSensorMapper;

    @InjectMocks
    private BuscaLeituraService buscaLeituraService;

    @Test
    @DisplayName("Deve buscar leituras por ID de atendimento e mapeá-las para DTOs")
    void buscarPorAtendimento_shouldReturnMappedDTOs() {
        Long atendimentoId = 1L;
        LeituraSensor leitura1 = new LeituraSensor();
        leitura1.setId(1L);
        LeituraSensor leitura2 = new LeituraSensor();
        leitura2.setId(2L);
        List<LeituraSensor> leituras = Arrays.asList(leitura1, leitura2);

        LeituraSensorResponseDTO dto1 = new LeituraSensorResponseDTO();
        LeituraSensorResponseDTO dto2 = new LeituraSensorResponseDTO();
        List<LeituraSensorResponseDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(leituraSensorRepository.findByAtendimentoId(atendimentoId)).thenReturn(leituras);
        when(leituraSensorMapper.toResponse(leitura1)).thenReturn(dto1);
        when(leituraSensorMapper.toResponse(leitura2)).thenReturn(dto2);

        List<LeituraSensorResponseDTO> actualDTOs = buscaLeituraService.buscarPorAtendimento(atendimentoId);

        assertNotNull(actualDTOs);
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertEquals(expectedDTOs, actualDTOs);
        verify(leituraSensorRepository, times(1)).findByAtendimentoId(atendimentoId);
        verify(leituraSensorMapper, times(1)).toResponse(leitura1);
        verify(leituraSensorMapper, times(1)).toResponse(leitura2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar leituras por atendimento quando não houver leituras")
    void buscarPorAtendimento_shouldReturnEmptyList_whenNoLeiturasFound() {
        Long atendimentoId = 2L;
        when(leituraSensorRepository.findByAtendimentoId(atendimentoId)).thenReturn(Collections.emptyList());

        List<LeituraSensorResponseDTO> actualDTOs = buscaLeituraService.buscarPorAtendimento(atendimentoId);

        assertNotNull(actualDTOs);
        assertEquals(0, actualDTOs.size());
        verify(leituraSensorRepository, times(1)).findByAtendimentoId(atendimentoId);
        verify(leituraSensorMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve buscar todas as leituras e mapeá-las para DTOs")
    void buscarTodas_shouldReturnMappedDTOs() {
        LeituraSensor leitura1 = new LeituraSensor();
        leitura1.setId(1L); // Assign a distinct ID
        LeituraSensor leitura2 = new LeituraSensor();
        leitura2.setId(2L); // Assign a distinct ID
        List<LeituraSensor> leituras = Arrays.asList(leitura1, leitura2);

        LeituraSensorResponseDTO dto1 = new LeituraSensorResponseDTO();
        LeituraSensorResponseDTO dto2 = new LeituraSensorResponseDTO();
        List<LeituraSensorResponseDTO> expectedDTOs = Arrays.asList(dto1, dto2);

        when(leituraSensorRepository.findAll()).thenReturn(leituras);
        when(leituraSensorMapper.toResponse(leitura1)).thenReturn(dto1);
        when(leituraSensorMapper.toResponse(leitura2)).thenReturn(dto2);

        List<LeituraSensorResponseDTO> actualDTOs = buscaLeituraService.buscarTodas();

        assertNotNull(actualDTOs);
        assertEquals(expectedDTOs.size(), actualDTOs.size());
        assertEquals(expectedDTOs, actualDTOs);
        verify(leituraSensorRepository, times(1)).findAll();
        verify(leituraSensorMapper, times(1)).toResponse(leitura1);
        verify(leituraSensorMapper, times(1)).toResponse(leitura2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar todas as leituras quando não houver leituras")
    void buscarTodas_shouldReturnEmptyList_whenNoLeiturasFound() {
        when(leituraSensorRepository.findAll()).thenReturn(Collections.emptyList());

        List<LeituraSensorResponseDTO> actualDTOs = buscaLeituraService.buscarTodas();

        assertNotNull(actualDTOs);
        assertEquals(0, actualDTOs.size());
        verify(leituraSensorRepository, times(1)).findAll();
        verify(leituraSensorMapper, never()).toResponse(any());
    }
}
