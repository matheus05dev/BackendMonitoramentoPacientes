package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.leitura.BuscaLeituraService;
import com.springwalker.back.monitoramento.service.notificacao.BuscarNotificacaoService;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para MonitoramentoWSController")
class MonitoramentoWSControllerTest {

    @Mock
    private BuscaLeituraService buscaLeituraService;
    @Mock
    private GerenciadorNotificacaoService gerenciadorNotificacaoService;
    @Mock
    private BuscarNotificacaoService buscarNotificacaoService;
    @Mock
    private NotificacaoMapper notificacaoMapper;
    @Mock
    private Principal principal;

    @InjectMocks
    private MonitoramentoWSController monitoramentoWSController;

    @BeforeEach
    void setUp() {
        when(principal.getName()).thenReturn("testUser");
    }

    @Test
    @DisplayName("Deve retornar leituras para um atendimento específico")
    void buscarLeiturasPorAtendimento_shouldReturnLeituras() {
        Long atendimentoId = 1L;
        LeituraSensorResponseDTO leitura1 = new LeituraSensorResponseDTO();
        LeituraSensorResponseDTO leitura2 = new LeituraSensorResponseDTO();
        List<LeituraSensorResponseDTO> expectedLeituras = Arrays.asList(leitura1, leitura2);

        when(buscaLeituraService.buscarPorAtendimento(atendimentoId)).thenReturn(expectedLeituras);

        List<LeituraSensorResponseDTO> actualLeituras = monitoramentoWSController.buscarLeiturasPorAtendimento(atendimentoId, principal);

        assertNotNull(actualLeituras);
        assertEquals(expectedLeituras.size(), actualLeituras.size());
        assertEquals(expectedLeituras, actualLeituras);
        verify(buscaLeituraService, times(1)).buscarPorAtendimento(atendimentoId);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver leituras para o atendimento")
    void buscarLeiturasPorAtendimento_shouldReturnEmptyList_whenNoLeiturasFound() {
        Long atendimentoId = 2L;
        when(buscaLeituraService.buscarPorAtendimento(atendimentoId)).thenReturn(Collections.emptyList());

        List<LeituraSensorResponseDTO> actualLeituras = monitoramentoWSController.buscarLeiturasPorAtendimento(atendimentoId, principal);

        assertNotNull(actualLeituras);
        assertEquals(0, actualLeituras.size());
        verify(buscaLeituraService, times(1)).buscarPorAtendimento(atendimentoId);
    }

    @Test
    @DisplayName("Deve retornar histórico de notificações")
    void buscarHistoricoNotificacoes_shouldReturnNotificacoes() {
        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(2L);
        List<Notificacao> notificacoes = Arrays.asList(notificacao1, notificacao2);

        NotificacaoResponseDTO response1 = new NotificacaoResponseDTO();
        NotificacaoResponseDTO response2 = new NotificacaoResponseDTO();
        List<NotificacaoResponseDTO> expectedResponses = Arrays.asList(response1, response2);

        when(buscarNotificacaoService.buscarTodasNotificacoes()).thenReturn(notificacoes);
        when(notificacaoMapper.toResponse(notificacao1)).thenReturn(response1);
        when(notificacaoMapper.toResponse(notificacao2)).thenReturn(response2);

        List<NotificacaoResponseDTO> actualResponses = monitoramentoWSController.buscarHistoricoNotificacoes(principal);

        assertNotNull(actualResponses);
        assertEquals(expectedResponses.size(), actualResponses.size());
        assertEquals(expectedResponses, actualResponses);
        verify(buscarNotificacaoService, times(1)).buscarTodasNotificacoes();
        verify(notificacaoMapper, times(1)).toResponse(notificacao1);
        verify(notificacaoMapper, times(1)).toResponse(notificacao2);
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver notificações")
    void buscarHistoricoNotificacoes_shouldReturnEmptyList_whenNoNotificacoesFound() {
        when(buscarNotificacaoService.buscarTodasNotificacoes()).thenReturn(Collections.emptyList());

        List<NotificacaoResponseDTO> actualResponses = monitoramentoWSController.buscarHistoricoNotificacoes(principal);

        assertNotNull(actualResponses);
        assertEquals(0, actualResponses.size());
        verify(buscarNotificacaoService, times(1)).buscarTodasNotificacoes();
        verify(notificacaoMapper, never()).toResponse(any());
    }
}
