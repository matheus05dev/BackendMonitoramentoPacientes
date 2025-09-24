package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.core.enums.StatusNotificacao;
import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Endpoints para consultar e gerenciar o ciclo de vida das notificações de alerta")
public class NotificacaoRestController {

    private final GerenciadorNotificacaoService gerenciadorNotificacaoService;
    private final NotificacaoMapper notificacaoMapper;

    @GetMapping
    @Operation(summary = "Busca o histórico de notificações", description = "Retorna uma lista de todas as notificações geradas. Pode ser filtrado por status (ABERTA, EM_ATENDIMENTO, FECHADA).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de notificações retornado com sucesso")
    })
    public ResponseEntity<List<NotificacaoResponseDTO>> getNotificacoes(
            @Parameter(description = "Filtra as notificações por status") @RequestParam(required = false) StatusNotificacao status) {

        List<Notificacao> notificacoes;
        if (status != null) {
            notificacoes = gerenciadorNotificacaoService.buscarNotificacoesPorStatus(status);
        } else {
            notificacoes = gerenciadorNotificacaoService.buscarTodasNotificacoes();
        }

        List<NotificacaoResponseDTO> response = notificacoes.stream()
                .map(notificacaoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/fechar")
    @Operation(summary = "Fecha uma notificação de alerta", description = "Altera o status de uma notificação para 'FECHADA', indicando que o alerta foi atendido.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notificação fechada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    })
    public ResponseEntity<NotificacaoResponseDTO> fecharNotificacao(@PathVariable Long id) {
        return gerenciadorNotificacaoService.fecharNotificacao(id)
                .map(notificacao -> ResponseEntity.ok(notificacaoMapper.toResponse(notificacao)))
                .orElse(ResponseEntity.notFound().build());
    }
}
