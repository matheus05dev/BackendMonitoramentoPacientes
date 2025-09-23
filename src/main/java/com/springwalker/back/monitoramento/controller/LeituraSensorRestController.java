package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.services.leitura.BuscaLeituraService;
import com.springwalker.back.monitoramento.services.leitura.processamento.ProcessaDadosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
@Tag(name = "Leituras de Sensores", description = "Endpoints para receber e consultar leituras de sensores dos pacientes")
public class LeituraSensorRestController {

    private final ProcessaDadosService processaDadosService;
    private final BuscaLeituraService buscaLeituraService;

    @PostMapping("/atendimento/{atendimentoId}")
    @Operation(summary = "Salva uma nova leitura de sensor", description = "Recebe os dados de um sensor e os associa a um atendimento existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leitura salva e processada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor")
    })
    public ResponseEntity<LeituraSensorResponseDTO> salvarLeitura(
            @PathVariable Long atendimentoId,
            @RequestBody LeituraSensorRequestDTO requestDTO) {
        LeituraSensorResponseDTO responseDTO = processaDadosService.salvar(atendimentoId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/atendimento/{atendimentoId}")
    @Operation(summary = "Busca todas as leituras de um atendimento", description = "Retorna uma lista com todo o histórico de leituras de um atendimento específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de leituras retornada com sucesso")
    })
    public ResponseEntity<List<LeituraSensorResponseDTO>> buscarLeiturasPorAtendimento(@PathVariable Long atendimentoId) {
        List<LeituraSensorResponseDTO> leituras = buscaLeituraService.buscarPorAtendimento(atendimentoId);
        return ResponseEntity.ok(leituras);
    }

    @GetMapping
    @Operation(summary = "Busca todas as leituras registradas", description = "Retorna uma lista com todas as leituras de todos os atendimentos. Use com cautela em produção.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de todas as leituras retornada com sucesso")
    })
    public ResponseEntity<List<LeituraSensorResponseDTO>> buscarTodasLeituras() {
        List<LeituraSensorResponseDTO> leituras = buscaLeituraService.buscarTodas();
        return ResponseEntity.ok(leituras);
    }
}
