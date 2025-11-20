package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para FechadorNotificacaoService")
class FechadorNotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private FechadorNotificacaoService fechadorNotificacaoService;

    @Test
    @DisplayName("Deve fechar uma notificação aberta com sucesso")
    void fecharNotificacao_shouldCloseOpenNotificacaoSuccessfully() {
        Long notificacaoId = 1L;
        Notificacao notificacaoAberta = new Notificacao();
        notificacaoAberta.setId(notificacaoId);
        notificacaoAberta.setStatus(StatusNotificacao.ABERTA);
        notificacaoAberta.setDataCriacao(LocalDateTime.now().minusHours(1));

        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.of(notificacaoAberta));
        when(notificacaoRepository.save(any(Notificacao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Optional<Notificacao> result = fechadorNotificacaoService.fecharNotificacao(notificacaoId);

        assertTrue(result.isPresent());
        Notificacao fechada = result.get();
        assertEquals(StatusNotificacao.FECHADA, fechada.getStatus());
        assertNotNull(fechada.getDataFechamento());
        assertEquals(notificacaoId, fechada.getId());
        verify(notificacaoRepository, times(1)).findById(notificacaoId);
        verify(notificacaoRepository, times(1)).save(notificacaoAberta);
    }

    @Test
    @DisplayName("Não deve alterar uma notificação já fechada")
    void fecharNotificacao_shouldNotAlterAlreadyClosedNotificacao() {
        Long notificacaoId = 2L;
        Notificacao notificacaoFechada = new Notificacao();
        notificacaoFechada.setId(notificacaoId);
        notificacaoFechada.setStatus(StatusNotificacao.FECHADA);
        notificacaoFechada.setDataCriacao(LocalDateTime.now().minusHours(2));
        notificacaoFechada.setDataFechamento(LocalDateTime.now().minusHours(1));

        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.of(notificacaoFechada));

        Optional<Notificacao> result = fechadorNotificacaoService.fecharNotificacao(notificacaoId);

        assertTrue(result.isPresent());
        Notificacao unchanged = result.get();
        assertEquals(StatusNotificacao.FECHADA, unchanged.getStatus());
        assertEquals(notificacaoFechada.getDataFechamento(), unchanged.getDataFechamento()); // Should be the original one
        verify(notificacaoRepository, times(1)).findById(notificacaoId);
        verify(notificacaoRepository, never()).save(any(Notificacao.class)); // Should not save again
    }

    @Test
    @DisplayName("Deve retornar Optional vazio se a notificação não for encontrada")
    void fecharNotificacao_shouldReturnEmptyOptional_whenNotificacaoNotFound() {
        Long notificacaoId = 3L;

        when(notificacaoRepository.findById(notificacaoId)).thenReturn(Optional.empty());

        Optional<Notificacao> result = fechadorNotificacaoService.fecharNotificacao(notificacaoId);

        assertTrue(result.isEmpty());
        verify(notificacaoRepository, times(1)).findById(notificacaoId);
        verify(notificacaoRepository, never()).save(any(Notificacao.class));
    }
}
