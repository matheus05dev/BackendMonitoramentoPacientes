package com.springwalker.back.monitoramento.service.notificacao;

import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificacaoMapper notificacaoMapper;

    /**
     * Converte a entidade Notificacao para DTO e a envia via WebSocket para o tópico de notificações.
     * @param notificacao A entidade Notificacao que foi criada e salva.
     */
    public void enviarNotificacao(Notificacao notificacao) {
        if (notificacao != null) {
            System.out.println("Enviando notificação com ID: " + notificacao.getId() + " para o tópico /topic/notificacoes");

            // Converte a entidade Notificacao para DTO antes de enviar
            NotificacaoResponseDTO responseDTO = notificacaoMapper.toResponse(notificacao);

            // Envia o DTO para o tópico do WebSocket
            messagingTemplate.convertAndSend("/topic/notificacoes", responseDTO);
        }
    }
}
