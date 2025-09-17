package com.springwalker.back.monitoramento.controller;

import com.springwalker.back.monitoramento.dto.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.services.ProcessaDadosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/atendimentos")
@RequiredArgsConstructor
public class LeituraSensorController {

    private final ProcessaDadosService processaDadosService;

    @PostMapping("/{atendimentoId}/leituras")
    public ResponseEntity<LeituraSensorResponseDTO> salvarLeitura(
            @PathVariable Long atendimentoId,
            @RequestBody LeituraSensorRequestDTO requestDTO) {
        LeituraSensorResponseDTO responseDTO = processaDadosService.salvar(atendimentoId, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
