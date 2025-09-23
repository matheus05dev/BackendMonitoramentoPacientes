package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.services.leitura.BuscaLeituraService;
import com.springwalker.back.monitoramento.services.notificacao.processamento.GerenciadorNotificacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MonitoramentoWSController {

    private final BuscaLeituraService buscaLeituraService;
    private final GerenciadorNotificacaoService gerenciadorNotificacaoService;
    private final LeituraSensorMapper leituraSensorMapper;


     //Busca todas as leituras de um atendimento específico via WebSocket.
     //O cliente envia uma mensagem para /app/leituras/por-atendimento com o ID do atendimento no corpo.
     //A resposta é enviada de volta apenas para o usuário que fez a requisição.

    @MessageMapping("/leituras/por-atendimento")
    @SendToUser("/queue/leituras")
    public List<LeituraSensorResponseDTO> buscarLeiturasPorAtendimento(@Payload Long atendimentoId, Principal principal) {
        System.out.println("Recebida requisição WS de " + principal.getName() + " para buscar leituras do atendimento: " + atendimentoId);
        return buscaLeituraService.buscarPorAtendimento(atendimentoId);
    }

     //Busca o histórico de notificações (leituras com gravidade) via WebSocket.
     //O cliente envia uma mensagem para /app/notificacoes/historico.
     //A resposta é enviada de volta apenas para o usuário que fez a requisição.
    @MessageMapping("/notificacoes/historico")
    @SendToUser("/queue/notificacoes")
    public List<LeituraSensorResponseDTO> buscarHistoricoNotificacoes(Principal principal) {
        System.out.println("Recebida requisição WS de " + principal.getName() + " para buscar histórico de notificações.");
        List<LeituraSensor> notificacoes = gerenciadorNotificacaoService.buscarTodasNotificacoes();
        return notificacoes.stream()
                .map(leituraSensorMapper::toResponse)
                .collect(Collectors.toList());
    }
}
