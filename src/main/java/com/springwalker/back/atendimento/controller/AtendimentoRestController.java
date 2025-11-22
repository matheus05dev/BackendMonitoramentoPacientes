package com.springwalker.back.atendimento.controller;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.service.AlteraAtendimentoService;
import com.springwalker.back.atendimento.service.BuscaAtendimentoService;
import com.springwalker.back.atendimento.service.CriaAtendimentoService;
import com.springwalker.back.atendimento.service.DeletaAtendimentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/atendimento")
@Tag(name = "Atendimentos", description = "Gerenciamento de atendimentos médicos")
public class AtendimentoRestController {
    private final AlteraAtendimentoService alteraAtendimento;
    private final CriaAtendimentoService criaAtendimento;
    private final BuscaAtendimentoService buscaAtendimento;
    private final DeletaAtendimentoService deletaAtendimento;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Criar novo atendimento", description = "Cria um novo atendimento médico associando paciente e funcionário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Atendimento criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<AtendimentoResponseDTO> criaAtendimento(@Valid @RequestBody AtendimentoRequestDTO dto) {
        AtendimentoResponseDTO responseDTO = criaAtendimento.criarAtendimento(dto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar atendimento por ID", description = "Retorna os detalhes de um atendimento específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento encontrado"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    public ResponseEntity<AtendimentoResponseDTO> buscarAtendimentoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(buscaAtendimento.buscarAtendimentoPorId(id).orElseThrow(() -> new NoSuchElementException("Atendimento não encontrado com o ID: " + id)));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os atendimentos", description = "Retorna uma lista de todos os atendimentos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de atendimentos retornada com sucesso")
    public ResponseEntity<List<AtendimentoResponseDTO>> buscarTodosAtendimentos() {
        List<AtendimentoResponseDTO> dtos = buscaAtendimento.buscarTodosAtendimentos();
        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    @Operation(summary = "Alterar atendimento", description = "Atualiza os dados de um atendimento existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    public ResponseEntity<AtendimentoResponseDTO> alterarAtendimento(@PathVariable Long id,
            @Valid @RequestBody AtendimentoRequestDTO dto) {
        AtendimentoResponseDTO responseDTO = alteraAtendimento.alterarAtendimento(id, dto);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar atendimento", description = "Remove um atendimento do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Atendimento deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Atendimento não encontrado")
    })
    public ResponseEntity<AtendimentoResponseDTO> deletarAtendimento(@PathVariable Long id) {
        AtendimentoResponseDTO dto = deletaAtendimento.deletarAtendimento(id);
        return ResponseEntity.ok(dto);
    }
}
