package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.dto.notificacao.NotificacaoResponseDTO;
import com.springwalker.back.monitoramento.mapper.NotificacaoMapper;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.service.notificacao.BuscarNotificacaoService;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificacoes")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Endpoints para consultar e gerenciar o ciclo de vida das notificações de alerta")
public class NotificacaoRestController {

    private final GerenciadorNotificacaoService gerenciadorNotificacaoService;
    private final BuscarNotificacaoService buscarNotificacaoService;
    private final NotificacaoMapper notificacaoMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Busca o histórico de notificações", description = "Retorna uma lista de todas as notificações geradas. Pode ser filtrado por status (ABERTA, EM_ATENDIMENTO, FECHADA).")
    public ResponseEntity<List<NotificacaoResponseDTO>> getNotificacoes(
            @Parameter(description = "Filtra as notificações por status") @RequestParam(required = false) StatusNotificacao status) {

        List<Notificacao> notificacoes;
        if (status != null) {
            notificacoes = buscarNotificacaoService.buscarNotificacoesPorStatus(status);
        } else {
            notificacoes = buscarNotificacaoService.buscarTodasNotificacoes();
        }

        List<NotificacaoResponseDTO> response = notificacoes.stream()
                .map(notificacaoMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/fechar")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM')")
    @Operation(summary = "Fecha uma notificação de alerta", description = "Altera o status de uma notificação para 'FECHADA', indicando que o alerta foi atendido.")
    public ResponseEntity<NotificacaoResponseDTO> fecharNotificacao(@PathVariable Long id) {
        return ResponseEntity.ok(gerenciadorNotificacaoService.fecharNotificacao(id)
                .map(notificacaoMapper::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Notificação não encontrada com o ID: " + id)));
    }
}
