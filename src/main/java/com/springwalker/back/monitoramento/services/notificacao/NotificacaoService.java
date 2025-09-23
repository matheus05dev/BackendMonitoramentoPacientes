package com.springwalker.back.monitoramento.services.notificacao;

import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificacaoService {

    private final SimpMessagingTemplate messagingTemplate;
    private final LeituraSensorMapper leituraSensorMapper;

    //Converte a entidade LeituraSensor para DTO e a envia via WebSocket.
    public void enviarNotificacao(LeituraSensor leitura) {
        if (leitura != null && leitura.getGravidade() != null) {
            System.out.println("Enviando notificação para a leitura: " + leitura.getId());

            // Converte a entidade para DTO antes de enviar
            LeituraSensorResponseDTO responseDTO = leituraSensorMapper.toResponse(leitura);

            // Envia o DTO para o tópico do WebSocket
            messagingTemplate.convertAndSend("/topic/notificacoes", responseDTO);
        }
    }
}
