package com.springwalker.back.monitoramento.service.notificacao;

import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para EnviadorNotificacaoService")
class EnviadorNotificacaoServiceTest {

    @Mock
    private SimpMessagingTemplate messagingTemplate;
    @Mock
    private NotificacaoMapper notificacaoMapper;

    @InjectMocks
    private EnviadorNotificacaoService enviadorNotificacaoService;

    @Test
    @DisplayName("Deve enviar notificação quando a notificação não for nula")
    void enviarNotificacao_shouldSendNotification_whenNotificacaoIsNotNull() {
        Notificacao notificacao = new Notificacao();
        notificacao.setId(1L);
        NotificacaoResponseDTO responseDTO = new NotificacaoResponseDTO();
        responseDTO.setId(1L);

        when(notificacaoMapper.toResponse(notificacao)).thenReturn(responseDTO);

        enviadorNotificacaoService.enviarNotificacao(notificacao);

        verify(notificacaoMapper, times(1)).toResponse(notificacao);
        verify(messagingTemplate, times(1)).convertAndSend("/topic/notificacoes", responseDTO);
    }

    @Test
    @DisplayName("Não deve enviar notificação quando a notificação for nula")
    void enviarNotificacao_shouldNotSendNotification_whenNotificacaoIsNull() {
        enviadorNotificacaoService.enviarNotificacao(null);

        verify(notificacaoMapper, never()).toResponse(any());

        verify(messagingTemplate, never()).convertAndSend(anyString(), any(NotificacaoResponseDTO.class));
    }
}
