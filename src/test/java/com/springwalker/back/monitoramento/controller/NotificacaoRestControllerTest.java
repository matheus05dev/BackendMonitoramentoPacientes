package com.springwalker.back.monitoramento.controller;


import com.springwalker.back.core.log.service.LogService;
import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.notificacao.BuscarNotificacaoService;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para NotificacaoRestController")
class NotificacaoRestControllerTest {

    @Mock
    private GerenciadorNotificacaoService gerenciadorNotificacaoService;
    @Mock
    private BuscarNotificacaoService buscarNotificacaoService;
    @Mock
    private NotificacaoMapper notificacaoMapper;
    @Mock
    private LogService logService; // Changed from LogRepository

    @InjectMocks
    private NotificacaoRestController notificacaoRestController;

    @Test
    @DisplayName("Deve retornar todas as notificações quando nenhum status for fornecido")
    void getNotificacoes_shouldReturnAllNotificacoes_whenNoStatusProvided() {
        doNothing().when(logService).logEvent(anyString(), anyString());

        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(2L);
        List<Notificacao> notificacoes = Arrays.asList(notificacao1, notificacao2);

        NotificacaoResponseDTO response1 = new NotificacaoResponseDTO();
        response1.setId(1L);
        NotificacaoResponseDTO response2 = new NotificacaoResponseDTO();
        response2.setId(2L);
        List<NotificacaoResponseDTO> expectedResponses = Arrays.asList(response1, response2);

        when(buscarNotificacaoService.buscarTodasNotificacoes()).thenReturn(notificacoes);
        when(notificacaoMapper.toResponse(notificacao1)).thenReturn(response1);
        when(notificacaoMapper.toResponse(notificacao2)).thenReturn(response2);

        ResponseEntity<List<NotificacaoResponseDTO>> responseEntity = notificacaoRestController.getNotificacoes(null);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody()); // Added null check
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponses.size(), responseEntity.getBody().size());
        assertEquals(expectedResponses, responseEntity.getBody());
        verify(buscarNotificacaoService, times(1)).buscarTodasNotificacoes();
        verify(buscarNotificacaoService, never()).buscarNotificacoesPorStatus(any());
        verify(notificacaoMapper, times(1)).toResponse(notificacao1);
        verify(notificacaoMapper, times(1)).toResponse(notificacao2);
        verify(logService, times(1)).logEvent(anyString(), anyString()); // Changed from logRepository.save(any(Log.class))
    }

    @Test
    @DisplayName("Deve retornar notificações filtradas por status")
    void getNotificacoes_shouldReturnFilteredNotificacoes_whenStatusProvided() {
        doNothing().when(logService).logEvent(anyString(), anyString());

        StatusNotificacao status = StatusNotificacao.ABERTA;
        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        notificacao1.setStatus(status);
        List<Notificacao> notificacoes = Collections.singletonList(notificacao1);

        NotificacaoResponseDTO response1 = new NotificacaoResponseDTO();
        response1.setId(1L);
        response1.setStatus(status);
        List<NotificacaoResponseDTO> expectedResponses = Collections.singletonList(response1);

        when(buscarNotificacaoService.buscarNotificacoesPorStatus(status)).thenReturn(notificacoes);
        when(notificacaoMapper.toResponse(notificacao1)).thenReturn(response1);

        ResponseEntity<List<NotificacaoResponseDTO>> responseEntity = notificacaoRestController.getNotificacoes(status);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody()); // Added null check
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponses.size(), responseEntity.getBody().size());
        assertEquals(expectedResponses, responseEntity.getBody());
        verify(buscarNotificacaoService, times(1)).buscarNotificacoesPorStatus(status);
        verify(buscarNotificacaoService, never()).buscarTodasNotificacoes();
        verify(notificacaoMapper, times(1)).toResponse(notificacao1);
        verify(logService, times(1)).logEvent(anyString(), anyString()); // Changed from logRepository.save(any(Log.class))
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver notificações")
    void getNotificacoes_shouldReturnEmptyList_whenNoNotificacoesFound() {
        doNothing().when(logService).logEvent(anyString(), anyString());

        when(buscarNotificacaoService.buscarTodasNotificacoes()).thenReturn(Collections.emptyList());

        ResponseEntity<List<NotificacaoResponseDTO>> responseEntity = notificacaoRestController.getNotificacoes(null);

        assertNotNull(responseEntity);
        assertNotNull(responseEntity.getBody()); // Added null check
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(0, responseEntity.getBody().size());
        verify(buscarNotificacaoService, times(1)).buscarTodasNotificacoes();
        verify(notificacaoMapper, never()).toResponse(any());
        verify(logService, times(1)).logEvent(anyString(), anyString()); // Changed from logRepository.save(any(Log.class))
    }

    @Test
    @DisplayName("Deve fechar uma notificação com sucesso")
    void fecharNotificacao_shouldCloseNotificacaoSuccessfully() {
        doNothing().when(logService).logEvent(anyString(), anyString());

        Long notificacaoId = 1L;
        Notificacao notificacaoFechada = new Notificacao();
        notificacaoFechada.setId(notificacaoId);
        notificacaoFechada.setStatus(StatusNotificacao.FECHADA);

        NotificacaoResponseDTO expectedResponse = new NotificacaoResponseDTO();
        expectedResponse.setId(notificacaoId);
        expectedResponse.setStatus(StatusNotificacao.FECHADA);

        when(gerenciadorNotificacaoService.fecharNotificacao(notificacaoId)).thenReturn(Optional.of(notificacaoFechada));
        when(notificacaoMapper.toResponse(notificacaoFechada)).thenReturn(expectedResponse);

        ResponseEntity<NotificacaoResponseDTO> responseEntity = notificacaoRestController.fecharNotificacao(notificacaoId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
        verify(gerenciadorNotificacaoService, times(1)).fecharNotificacao(notificacaoId);
        verify(notificacaoMapper, times(1)).toResponse(notificacaoFechada);
        verify(logService, times(1)).logEvent(anyString(), anyString()); // Changed from logRepository.save(any(Log.class))
    }

    @Test
    @DisplayName("Deve retornar 404 quando a notificação a ser fechada não for encontrada")
    void fecharNotificacao_shouldReturnNotFound_whenNotificacaoNotFound() {
        doNothing().when(logService).logEvent(anyString(), anyString());

        Long notificacaoId = 1L;

        when(gerenciadorNotificacaoService.fecharNotificacao(notificacaoId)).thenReturn(Optional.empty());

        ResponseEntity<NotificacaoResponseDTO> responseEntity = notificacaoRestController.fecharNotificacao(notificacaoId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        verify(gerenciadorNotificacaoService, times(1)).fecharNotificacao(notificacaoId);
        verify(notificacaoMapper, never()).toResponse(any());
        verify(logService, times(1)).logEvent(anyString(), anyString()); // Changed from logRepository.save(any(Log.class))
    }
}
