package com.springwalker.back.quarto.controller;

import com.springwalker.back.quarto.dto.QuartoRequestDTO;
import com.springwalker.back.quarto.dto.QuartoResponseDTO;
import com.springwalker.back.quarto.mapper.QuartoMapper;
import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os quartos", description = "Retorna uma lista de todos os quartos cadastrados")
    public List<QuartoResponseDTO> listar() {
        return buscaQuartoService.listarTodos();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar quarto por ID", description = "Retorna os detalhes de um quarto específico pelo ID")
    public QuartoResponseDTO buscarPorId(@PathVariable Long id) {
        return buscaQuartoService.buscarPorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo quarto", description = "Cria um novo quarto no sistema")
    public ResponseEntity<QuartoResponseDTO> inserir(@RequestBody QuartoRequestDTO dto) {
        QuartoResponseDTO novoQuarto = criaQuartoService.inserir(dto);
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar quarto", description = "Atualiza os dados de um quarto existente")
    public QuartoResponseDTO alterar(@PathVariable Long id, @RequestBody QuartoRequestDTO dto) {
        return alteraQuartoService.alterar(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar quarto", description = "Remove um quarto do sistema")
    public void excluir(@PathVariable Long id) {
        deletaQuartoService.excluir(id);
    }

    @PostMapping("/inserir-varios")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar vários quartos", description = "Cria múltiplos quartos no sistema")
    public List<QuartoResponseDTO> inserirVarios(@RequestBody List<QuartoRequestDTO> dtos) {
        return criaQuartoService.inserirVarios(dtos);
    }

    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM')")
    @Operation(summary = "Alocar paciente ao quarto", description = "Atribui um paciente a um quarto específico")
    public QuartoResponseDTO alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = atribuiPacienteQuartoService.alocarPaciente(quartoId, pacienteId);
        return quartoMapper.toResponseDTO(quarto);
    }

    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM')")
    @Operation(summary = "Remover paciente do quarto", description = "Remove um paciente de um quarto específico")
    public QuartoResponseDTO removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = removePacienteQuartoService.removerPaciente(quartoId, pacienteId);
        return quartoMapper.toResponseDTO(quarto);
    }
}
