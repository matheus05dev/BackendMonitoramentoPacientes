package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.services.notificacao.processamento.GerenciadorNotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Notificações", description = "Endpoints para consultar o histórico de notificações")
public class NotificacaoRestController {

    private final GerenciadorNotificacaoService gerenciadorNotificacaoService;
    private final LeituraSensorMapper leituraSensorMapper;

    @GetMapping("/notificacoes")
    @Operation(summary = "Busca o histórico de notificações", description = "Retorna uma lista de todas as leituras de sensores que geraram uma notificação (ou seja, com gravidade diferente de 'NORMAL').")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Histórico de notificações retornado com sucesso")
    })
    public ResponseEntity<List<LeituraSensorResponseDTO>> getNotificacoes() {
        List<LeituraSensor> notificacoes = gerenciadorNotificacaoService.buscarTodasNotificacoes();
        List<LeituraSensorResponseDTO> response = notificacoes.stream()
                .map(leituraSensorMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
