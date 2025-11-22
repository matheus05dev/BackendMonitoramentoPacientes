package com.springwalker.back.funcionario.controller;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.service.AlteraFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.BuscarFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.CriaFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.DeletaFuncionarioSaudeService;
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

@RestController
@RequestMapping("/api/funcionario")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Gerenciamento de funcionários de saúde")
public class FuncionarioSaudeRestController {

    private final BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;
    private final AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;
    private final CriaFuncionarioSaudeService criaFuncionarioSaudeService;
    private final DeletaFuncionarioSaudeService deletaFuncionarioSaudeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os funcionários", description = "Retorna uma lista de todos os funcionários de saúde cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    public List<FuncionarioSaudeResponseDTO> listar() {
        return buscarFuncionarioSaudeService.listarTodos();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo funcionário", description = "Cria um novo funcionário de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida, por exemplo, CPF já cadastrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> inserir(@Valid @RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        FuncionarioSaudeResponseDTO novoFuncionario = criaFuncionarioSaudeService.execute(funcionarioSaude);
        return new ResponseEntity<>(novoFuncionario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar funcionário", description = "Atualiza os dados de um funcionário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> alterar(@PathVariable Long id,
            @Valid @RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        FuncionarioSaudeResponseDTO funcionarioAtualizado = alteraFuncionarioSaudeService.execute(id, funcionarioSaude);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar funcionário", description = "Remove um funcionário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        deletaFuncionarioSaudeService.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMERO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionário por ID", description = "Retorna os detalhes de um funcionário específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(buscarFuncionarioSaudeService.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado com o ID: " + id)));
    }

    @GetMapping("/buscar-por-nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionários por nome", description = "Retorna uma lista de funcionários que correspondem ao nome informado")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    public List<FuncionarioSaudeResponseDTO> buscarPorNome(@PathVariable String nome) {
        return buscarFuncionarioSaudeService.buscarPorNome(nome);
    }

    @GetMapping("/buscar-por-cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionário por CPF", description = "Retorna os detalhes de um funcionário específico pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(buscarFuncionarioSaudeService.buscarPorCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado com o CPF: " + cpf)));
    }
}
