package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.services.BuscaLeituraService;
import com.springwalker.back.monitoramento.services.processamento.ProcessaDadosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leituras")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final ProcessaDadosService processaDadosService;
    private final BuscaLeituraService buscaLeituraService;

    // Recebe Leitura
    @PostMapping("/atendimento/{atendimentoId}")
    public ResponseEntity<LeituraSensorResponseDTO> salvarLeitura(
            @PathVariable Long atendimentoId,
            @RequestBody LeituraSensorRequestDTO requestDTO) {
        LeituraSensorResponseDTO responseDTO = processaDadosService.salvar(atendimentoId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    // Busca Leitura de apenas um atendimento especifico
    @GetMapping("/atendimento/{atendimentoId}")
    public ResponseEntity<List<LeituraSensorResponseDTO>> buscarLeiturasPorAtendimento(@PathVariable Long atendimentoId) {
        List<LeituraSensorResponseDTO> leituras = buscaLeituraService.buscarPorAtendimento(atendimentoId);
        return ResponseEntity.ok(leituras);
    }

    // Busca todas Leituras
    @GetMapping
    public ResponseEntity<List<LeituraSensorResponseDTO>> buscarTodasLeituras() {
        List<LeituraSensorResponseDTO> leituras = buscaLeituraService.buscarTodas();
        return ResponseEntity.ok(leituras);
    }
}
