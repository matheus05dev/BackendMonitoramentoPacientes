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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/funcionario")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Gerenciamento de funcionários de saúde")
public class FuncionarioSaudeRestController {

    private final BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;
    private final AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;
    private final CriaFuncionarioSaudeService criaFuncionarioSaudeService;
    private final DeletaFuncionarioSaudeService deletaFuncionarioSaudeService;

    // Buscar todos os funcionários
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os funcionários", description = "Retorna uma lista de todos os funcionários de saúde cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    public List<FuncionarioSaudeResponseDTO> listar() {
        return buscarFuncionarioSaudeService.listarTodos();
    }

    // Inserir um novo funcionário
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Criar novo funcionário", description = "Cria um novo funcionário de saúde no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida, por exemplo, CPF já cadastrado")
    })
    public ResponseEntity<?> inserir(@RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        try {
            FuncionarioSaudeResponseDTO novoFuncionario = criaFuncionarioSaudeService.execute(funcionarioSaude);
            return ResponseEntity.ok(novoFuncionario);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", e.getMessage()));
        }
    }

    // Alterar um funcionário por ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Alterar funcionário", description = "Atualiza os dados de um funcionário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> alterar(@PathVariable Long id,
            @RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        try {
            FuncionarioSaudeResponseDTO funcionarioAtualizado = alteraFuncionarioSaudeService.execute(id,
                    funcionarioSaude);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir um funcionário por ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar funcionário", description = "Remove um funcionário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        try {
            deletaFuncionarioSaudeService.execute(id);
            return ResponseEntity.ok().build(); // Return 200 OK for successful deletion
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if employee not found
        }
    }

    // Buscar um funcionário por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionário por ID", description = "Retorna os detalhes de um funcionário específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorId(@PathVariable Long id) {
        return buscarFuncionarioSaudeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar funcionários por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionários por nome", description = "Retorna uma lista de funcionários que correspondem ao nome informado")
    @ApiResponse(responseCode = "200", description = "Lista de funcionários retornada com sucesso")
    public List<FuncionarioSaudeResponseDTO> buscarPorNome(@PathVariable String nome) {
        return buscarFuncionarioSaudeService.buscarPorNome(nome);
    }

    // Buscar um funcionário por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar funcionário por CPF", description = "Retorna os detalhes de um funcionário específico pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return buscarFuncionarioSaudeService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
