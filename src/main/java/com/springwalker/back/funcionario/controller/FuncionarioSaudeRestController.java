package com.springwalker.back.funcionario.controller;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.service.AlteraFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.BuscarFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.CriaFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.DelataFuncionarioSaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionario")
@RequiredArgsConstructor
public class FuncionarioSaudeRestController {

    private final BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;
    private final AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;
    private final CriaFuncionarioSaudeService criaFuncionarioSaudeService;
    private final DelataFuncionarioSaudeService delataFuncionarioSaudeService;

    // Buscar todos os funcionários
    @GetMapping
    public List<FuncionarioSaudeResponseDTO> listar() {
        return buscarFuncionarioSaudeService.listarTodos();
    }

    // Inserir um novo funcionário
    @PostMapping
    public FuncionarioSaudeResponseDTO inserir(@RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        return criaFuncionarioSaudeService.execute(funcionarioSaude);
    }

    // Alterar um funcionário por ID
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioSaudeResponseDTO> alterar(@PathVariable Long id, @RequestBody FuncionarioSaudeRequestDTO funcionarioSaude) {
        try {
            FuncionarioSaudeResponseDTO funcionarioAtualizado = alteraFuncionarioSaudeService.execute(id, funcionarioSaude);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir um funcionário por ID
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        delataFuncionarioSaudeService.execute(id);
    }

    // Buscar um funcionário por ID
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorId(@PathVariable Long id) {
        return buscarFuncionarioSaudeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar funcionários por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    public List<FuncionarioSaudeResponseDTO> buscarPorNome(@PathVariable String nome) {
        return buscarFuncionarioSaudeService.buscarPorNome(nome);
    }

    // Buscar um funcionário por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarPorCpf(@PathVariable String cpf) {
        return buscarFuncionarioSaudeService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}