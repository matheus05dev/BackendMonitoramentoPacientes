package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.service.leitura.BuscaLeituraService;
import com.springwalker.back.monitoramento.service.leitura.processamento.ProcessaDadosService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para LeituraSensorRestController")
class LeituraSensorRestControllerTest {

    @Mock
    private ProcessaDadosService processaDadosService;
    @Mock
    private BuscaLeituraService buscaLeituraService;

    @InjectMocks
    private LeituraSensorRestController leituraSensorRestController;

    @Test
    @DisplayName("Deve salvar uma nova leitura de sensor com sucesso")
    void salvarLeitura_shouldSaveNewLeituraSuccessfully() {
        Long atendimentoId = 1L;
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();
        LeituraSensorResponseDTO expectedResponseDTO = new LeituraSensorResponseDTO();
        expectedResponseDTO.setId(1L);

        when(processaDadosService.salvar(atendimentoId, requestDTO)).thenReturn(expectedResponseDTO);

        ResponseEntity<LeituraSensorResponseDTO> responseEntity = leituraSensorRestController.salvarLeitura(atendimentoId, requestDTO);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponseDTO, responseEntity.getBody());
        verify(processaDadosService, times(1)).salvar(atendimentoId, requestDTO);
    }

    @Test
    @DisplayName("Deve buscar leituras por atendimento com sucesso")
    void buscarLeiturasPorAtendimento_shouldReturnLeiturasSuccessfully() {
        Long atendimentoId = 1L;
        LeituraSensorResponseDTO leitura1 = new LeituraSensorResponseDTO();
        leitura1.setId(1L);
        LeituraSensorResponseDTO leitura2 = new LeituraSensorResponseDTO();
        leitura2.setId(2L);
        List<LeituraSensorResponseDTO> expectedLeituras = Arrays.asList(leitura1, leitura2);

        when(buscaLeituraService.buscarPorAtendimento(atendimentoId)).thenReturn(expectedLeituras);

        ResponseEntity<List<LeituraSensorResponseDTO>> responseEntity = leituraSensorRestController.buscarLeiturasPorAtendimento(atendimentoId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedLeituras.size(), responseEntity.getBody().size());
        assertEquals(expectedLeituras, responseEntity.getBody());
        verify(buscaLeituraService, times(1)).buscarPorAtendimento(atendimentoId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar leituras por atendimento quando não houver leituras")
    void buscarLeiturasPorAtendimento_shouldReturnEmptyList_whenNoLeiturasFound() {
        Long atendimentoId = 2L;
        when(buscaLeituraService.buscarPorAtendimento(atendimentoId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<LeituraSensorResponseDTO>> responseEntity = leituraSensorRestController.buscarLeiturasPorAtendimento(atendimentoId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
        verify(buscaLeituraService, times(1)).buscarPorAtendimento(atendimentoId);
    }

    @Test
    @DisplayName("Deve buscar todas as leituras com sucesso")
    void buscarTodasLeituras_shouldReturnAllLeiturasSuccessfully() {
        LeituraSensorResponseDTO leitura1 = new LeituraSensorResponseDTO();
        leitura1.setId(1L);
        LeituraSensorResponseDTO leitura2 = new LeituraSensorResponseDTO();
        leitura2.setId(2L);
        List<LeituraSensorResponseDTO> expectedLeituras = Arrays.asList(leitura1, leitura2);

        when(buscaLeituraService.buscarTodas()).thenReturn(expectedLeituras);

        ResponseEntity<List<LeituraSensorResponseDTO>> responseEntity = leituraSensorRestController.buscarTodasLeituras();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedLeituras.size(), responseEntity.getBody().size());
        assertEquals(expectedLeituras, responseEntity.getBody());
        verify(buscaLeituraService, times(1)).buscarTodas();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar todas as leituras quando não houver leituras")
    void buscarTodasLeituras_shouldReturnEmptyList_whenNoLeiturasFound() {
        when(buscaLeituraService.buscarTodas()).thenReturn(Collections.emptyList());

        ResponseEntity<List<LeituraSensorResponseDTO>> responseEntity = leituraSensorRestController.buscarTodasLeituras();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
        verify(buscaLeituraService, times(1)).buscarTodas();
    }
}
