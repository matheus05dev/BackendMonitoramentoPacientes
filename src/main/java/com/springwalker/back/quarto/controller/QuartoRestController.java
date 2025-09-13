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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

    // Buscar todos os quartos
    @GetMapping
    @Operation(summary = "Listar todos os quartos", description = "Retorna uma lista de todos os quartos cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de quartos retornada com sucesso")
    public List<QuartoResponseDTO> listar() {
        return buscaQuartoService.listarTodos();
    }

    // Buscar um quarto por ID
    @GetMapping("/{id}")
    @Operation(summary = "Buscar quarto por ID", description = "Retorna os detalhes de um quarto específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quarto encontrado"),
            @ApiResponse(responseCode = "404", description = "Quarto não encontrado")
    })
    public QuartoResponseDTO buscarPorId(@PathVariable Long id) {
        return buscaQuartoService.buscarPorId(id);
    }

    // Inserir um novo quarto
    @PostMapping
    @Operation(summary = "Criar novo quarto", description = "Cria um novo quarto no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Quarto criado com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    public ResponseEntity<QuartoResponseDTO> inserir(@RequestBody QuartoRequestDTO dto) {
        QuartoResponseDTO novoQuarto = criaQuartoService.inserir(dto);
        if (novoQuarto == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao criar o quarto.");
        }
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    // Alterar um quarto
    @PutMapping("/{id}")
    @Operation(summary = "Alterar quarto", description = "Atualiza os dados de um quarto existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Quarto alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Quarto não encontrado")
    })
    public QuartoResponseDTO alterar(@PathVariable Long id, @RequestBody QuartoRequestDTO dto) {
        QuartoResponseDTO alterado = alteraQuartoService.alterar(id, dto);
        if (alterado == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + id);
        }
        return alterado;
    }

    // Apagar um quarto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deletar quarto", description = "Remove um quarto do sistema")
    @ApiResponse(responseCode = "204", description = "Quarto deletado com sucesso")
    public void excluir(@PathVariable Long id) {
        if (deletaQuartoService == null) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de deleção não disponível.");
        }
        deletaQuartoService.excluir(id);
    }

    // Inserir vários quartos
    @PostMapping("/inserir-varios")
    @Operation(summary = "Criar vários quartos", description = "Cria múltiplos quartos no sistema")
    @ApiResponse(responseCode = "200", description = "Quartos criados com sucesso")
    public List<QuartoResponseDTO> inserirVarios(@RequestBody List<QuartoRequestDTO> dtos) {
        return criaQuartoService.inserirVarios(dtos);
    }

    // Endpoint para alocar paciente em um quarto
    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    @Operation(summary = "Alocar paciente ao quarto", description = "Atribui um paciente a um quarto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente alocado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível alocar o paciente")
    })
    public QuartoResponseDTO alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = atribuiPacienteQuartoService.alocarPaciente(quartoId, pacienteId);
        if (quarto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível alocar o paciente ao quarto.");
        }
        return quartoMapper.toResponseDTO(quarto);
    }

    // Endpoint para remover paciente de um quarto
    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    @Operation(summary = "Remover paciente do quarto", description = "Remove um paciente de um quarto específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Não foi possível remover o paciente")
    })
    public QuartoResponseDTO removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = removePacienteQuartoService.removerPaciente(quartoId, pacienteId);
        if (quarto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não foi possível remover o paciente do quarto.");
        }
        return quartoMapper.toResponseDTO(quarto);
    }
}