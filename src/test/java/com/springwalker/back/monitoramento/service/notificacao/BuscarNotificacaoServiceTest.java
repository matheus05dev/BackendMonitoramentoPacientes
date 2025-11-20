package com.springwalker.back.monitoramento.service.notificacao;

import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para BuscarNotificacaoService")
class BuscarNotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private BuscarNotificacaoService buscarNotificacaoService;

    @Test
    @DisplayName("Deve buscar todas as notificações com detalhes")
    void buscarTodasNotificacoes_shouldReturnAllNotificacoesWithDetails() {
        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        Notificacao notificacao2 = new Notificacao();
        notificacao2.setId(2L);
        List<Notificacao> expectedNotificacoes = Arrays.asList(notificacao1, notificacao2);

        when(notificacaoRepository.findAllWithDetails()).thenReturn(expectedNotificacoes);

        List<Notificacao> actualNotificacoes = buscarNotificacaoService.buscarTodasNotificacoes();

        assertNotNull(actualNotificacoes);
        assertEquals(expectedNotificacoes.size(), actualNotificacoes.size());
        assertEquals(expectedNotificacoes, actualNotificacoes);
        verify(notificacaoRepository, times(1)).findAllWithDetails();
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar todas as notificações quando não houver nenhuma")
    void buscarTodasNotificacoes_shouldReturnEmptyList_whenNoNotificacoesFound() {
        when(notificacaoRepository.findAllWithDetails()).thenReturn(Collections.emptyList());

        List<Notificacao> actualNotificacoes = buscarNotificacaoService.buscarTodasNotificacoes();

        assertNotNull(actualNotificacoes);
        assertEquals(0, actualNotificacoes.size());
        verify(notificacaoRepository, times(1)).findAllWithDetails();
    }

    @Test
    @DisplayName("Deve buscar notificações por status")
    void buscarNotificacoesPorStatus_shouldReturnNotificacoesWithGivenStatus() {
        StatusNotificacao status = StatusNotificacao.ABERTA;
        Notificacao notificacao1 = new Notificacao();
        notificacao1.setId(1L);
        notificacao1.setStatus(status);
        List<Notificacao> expectedNotificacoes = Collections.singletonList(notificacao1);

        when(notificacaoRepository.findByStatus(status)).thenReturn(expectedNotificacoes);

        List<Notificacao> actualNotificacoes = buscarNotificacaoService.buscarNotificacoesPorStatus(status);

        assertNotNull(actualNotificacoes);
        assertEquals(expectedNotificacoes.size(), actualNotificacoes.size());
        assertEquals(expectedNotificacoes, actualNotificacoes);
        verify(notificacaoRepository, times(1)).findByStatus(status);
    }

    @Test
    @DisplayName("Deve retornar lista vazia ao buscar notificações por status quando não houver nenhuma")
    void buscarNotificacoesPorStatus_shouldReturnEmptyList_whenNoNotificacoesFound() {
        StatusNotificacao status = StatusNotificacao.EM_ATENDIMENTO;
        when(notificacaoRepository.findByStatus(status)).thenReturn(Collections.emptyList());

        List<Notificacao> actualNotificacoes = buscarNotificacaoService.buscarNotificacoesPorStatus(status);

        assertNotNull(actualNotificacoes);
        assertEquals(0, actualNotificacoes.size());
        verify(notificacaoRepository, times(1)).findByStatus(status);
    }

    @Test
    @DisplayName("Deve buscar notificação aberta por atendimento e tipo de dado")
    void buscarNotificacaoAbertaPorAtendimentoETipoDado_shouldReturnNotificacao() {
        Long atendimentoId = 1L;
        TipoDado tipoDado = TipoDado.TEMPERATURA;
        Notificacao expectedNotificacao = new Notificacao();
        expectedNotificacao.setId(1L);

        when(notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimentoId, tipoDado, StatusNotificacao.ABERTA)).thenReturn(Optional.of(expectedNotificacao));

        Optional<Notificacao> actualNotificacao = buscarNotificacaoService.buscarNotificacaoAbertaPorAtendimentoETipoDado(atendimentoId, tipoDado);

        assertTrue(actualNotificacao.isPresent());
        assertEquals(expectedNotificacao, actualNotificacao.get());
        verify(notificacaoRepository, times(1)).findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimentoId, tipoDado, StatusNotificacao.ABERTA);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio ao buscar notificação aberta por atendimento e tipo de dado quando não houver nenhuma")
    void buscarNotificacaoAbertaPorAtendimentoETipoDado_shouldReturnEmptyOptional_whenNoNotificacaoFound() {
        Long atendimentoId = 1L;
        TipoDado tipoDado = TipoDado.TEMPERATURA;

        when(notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimentoId, tipoDado, StatusNotificacao.ABERTA)).thenReturn(Optional.empty());

        Optional<Notificacao> actualNotificacao = buscarNotificacaoService.buscarNotificacaoAbertaPorAtendimentoETipoDado(atendimentoId, tipoDado);

        assertTrue(actualNotificacao.isEmpty());
        verify(notificacaoRepository, times(1)).findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimentoId, tipoDado, StatusNotificacao.ABERTA);
    }
}
