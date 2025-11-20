package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.notificacao.BuscarNotificacaoService;
import com.springwalker.back.monitoramento.service.notificacao.EnviadorNotificacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any; // Adicionado
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para GerenciadorNotificacaoService")
class GerenciadorNotificacaoServiceTest {

    @Mock
    private CriadorNotificacaoService criadorNotificacaoService;
    @Mock
    private FechadorNotificacaoService fechadorNotificacaoService;
    @Mock
    private EnviadorNotificacaoService enviadorNotificacaoService;
    @Mock
    private BuscarNotificacaoService buscarNotificacaoService;

    @InjectMocks
    private GerenciadorNotificacaoService gerenciadorNotificacaoService;

    @Test
    @DisplayName("Deve criar e enviar notificação se grave e não houver notificação existente")
    void processarEEnviarNotificacao_shouldCreateAndSend_whenGraveAndNoExistingNotification() {
        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);

        LeituraSensor leitura = LeituraSensor.builder()
                .gravidade(Gravidade.ALERTA)
                .tipoDado(TipoDado.TEMPERATURA)
                .atendimento(atendimento)
                .build();

        Notificacao savedNotificacao = new Notificacao();
        savedNotificacao.setId(10L);

        when(buscarNotificacaoService.buscarNotificacaoAbertaPorAtendimentoETipoDado(atendimento.getId(), leitura.getTipoDado()))
                .thenReturn(Optional.empty());
        when(criadorNotificacaoService.criarEGravarNotificacao(leitura)).thenReturn(savedNotificacao);

        gerenciadorNotificacaoService.processarEEnviarNotificacao(leitura);

        verify(buscarNotificacaoService, times(1)).buscarNotificacaoAbertaPorAtendimentoETipoDado(atendimento.getId(), leitura.getTipoDado());
        verify(criadorNotificacaoService, times(1)).criarEGravarNotificacao(leitura);
        verify(enviadorNotificacaoService, times(1)).enviarNotificacao(savedNotificacao);
    }

    @Test
    @DisplayName("Não deve criar nem enviar notificação se não for grave")
    void processarEEnviarNotificacao_shouldNotCreateOrSend_whenNotGrave() {
        LeituraSensor leitura = LeituraSensor.builder()
                .gravidade(Gravidade.NORMAL)
                .tipoDado(TipoDado.TEMPERATURA)
                .build();

        gerenciadorNotificacaoService.processarEEnviarNotificacao(leitura);

        verify(buscarNotificacaoService, never()).buscarNotificacaoAbertaPorAtendimentoETipoDado(any(), any());
        verify(criadorNotificacaoService, never()).criarEGravarNotificacao(any());
        verify(enviadorNotificacaoService, never()).enviarNotificacao(any());
    }

    @Test
    @DisplayName("Não deve criar nem enviar notificação se grave, mas já houver notificação existente")
    void processarEEnviarNotificacao_shouldNotCreateOrSend_whenGraveButExistingNotification() {
        Atendimento atendimento = new Atendimento();
        atendimento.setId(1L);

        LeituraSensor leitura = LeituraSensor.builder()
                .gravidade(Gravidade.ALERTA)
                .tipoDado(TipoDado.TEMPERATURA)
                .atendimento(atendimento)
                .build();

        Notificacao existingNotificacao = new Notificacao();
        existingNotificacao.setId(10L);

        when(buscarNotificacaoService.buscarNotificacaoAbertaPorAtendimentoETipoDado(atendimento.getId(), leitura.getTipoDado()))
                .thenReturn(Optional.of(existingNotificacao));

        gerenciadorNotificacaoService.processarEEnviarNotificacao(leitura);

        verify(buscarNotificacaoService, times(1)).buscarNotificacaoAbertaPorAtendimentoETipoDado(any(), any());
        verify(criadorNotificacaoService, never()).criarEGravarNotificacao(any());
        verify(enviadorNotificacaoService, never()).enviarNotificacao(any());
    }

    @Test
    @DisplayName("Deve delegar o fechamento da notificação ao FechadorNotificacaoService")
    void fecharNotificacao_shouldDelegateToFechadorNotificacaoService() {
        Long notificacaoId = 1L;
        Notificacao fechada = new Notificacao();
        fechada.setId(notificacaoId);

        when(fechadorNotificacaoService.fecharNotificacao(notificacaoId)).thenReturn(Optional.of(fechada));

        Optional<Notificacao> result = gerenciadorNotificacaoService.fecharNotificacao(notificacaoId);

        assertTrue(result.isPresent());
        assertEquals(fechada, result.get());
        verify(fechadorNotificacaoService, times(1)).fecharNotificacao(notificacaoId);
    }
}
