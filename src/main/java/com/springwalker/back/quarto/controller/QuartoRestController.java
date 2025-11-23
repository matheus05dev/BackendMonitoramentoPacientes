package com.springwalker.back.quarto.controller;

import com.springwalker.back.core.log.service.LogService; // Importar LogService
import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quarto")
@RequiredArgsConstructor
@Tag(name = "Quartos", description = "Gerenciamento de quartos hospitalares")
public class QuartoRestController {

    private final BuscaQuartoService buscaQuartoService;
    private final CriaQuartoService criaQuartoService;
    private final DeletaQuartoService deletaQuartoService;
    private final AlteraQuartoService alteraQuartoService;
    private final AtribuiPacienteQuartoService atribuiPacienteQuartoService;
    private final RemovePacienteQuartoService removePacienteQuartoService;
    private final QuartoMapper quartoMapper;
    private final LogService logService; // Injetar LogService

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os quartos", description = "Retorna uma lista de todos os quartos cadastrados")
    public List<QuartoResponseDTO> listar() {
        logService.logEvent("LISTAGEM_QUARTOS", "Listagem de todos os quartos");
        return buscaQuartoService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar quarto por ID", description = "Retorna os detalhes de um quarto específico pelo ID")
    public QuartoResponseDTO buscarPorId(@PathVariable Long id) {
        logService.logEvent("BUSCA_QUARTO", "Busca de quarto por ID: " + id);
        return buscaQuartoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo quarto", description = "Cria um novo quarto no sistema")
    public ResponseEntity<QuartoResponseDTO> inserir(@RequestBody QuartoRequestDTO dto) {
        QuartoResponseDTO novoQuarto = criaQuartoService.inserir(dto);
        logService.logEvent("CRIACAO_QUARTO", "Criação de novo quarto: " + novoQuarto.getId());
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar quarto", description = "Atualiza os dados de um quarto existente")
    public QuartoResponseDTO alterar(@PathVariable Long id, @RequestBody QuartoRequestDTO dto) {
        logService.logEvent("ALTERACAO_QUARTO", "Alteração de quarto: " + id);
        return alteraQuartoService.alterar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar quarto", description = "Remove um quarto do sistema")
    public void excluir(@PathVariable Long id) {
        logService.logEvent("EXCLUSAO_QUARTO", "Exclusão de quarto: " + id);
        deletaQuartoService.excluir(id);
    }

    @PostMapping("/inserir-varios")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar vários quartos", description = "Cria múltiplos quartos no sistema")
    public List<QuartoResponseDTO> inserirVarios(@RequestBody List<QuartoRequestDTO> dtos) {
        logService.logEvent("CRIACAO_MULTIPLOS_QUARTOS", "Criação de múltiplos quartos");
        return criaQuartoService.inserirVarios(dtos);
    }

    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM')")
    @Operation(summary = "Alocar paciente ao quarto", description = "Atribui um paciente a um quarto específico")
    public QuartoResponseDTO alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        logService.logEvent("ALOCACAO_PACIENTE_QUARTO", "Alocação de paciente " + pacienteId + " ao quarto " + quartoId);
        Quarto quarto = atribuiPacienteQuartoService.alocarPaciente(quartoId, pacienteId);
        return quartoMapper.toResponseDTO(quarto);
    }

    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM')")
    @Operation(summary = "Remover paciente do quarto", description = "Remove um paciente de um quarto específico")
    public QuartoResponseDTO removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        logService.logEvent("REMOCAO_PACIENTE_QUARTO", "Remoção de paciente " + pacienteId + " do quarto " + quartoId);
        Quarto quarto = removePacienteQuartoService.removerPaciente(quartoId, pacienteId);
        return quartoMapper.toResponseDTO(quarto);
    }
}
