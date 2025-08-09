package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.service.FuncionarioSaudeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")
public class FuncionarioSaudeRestController {

    @Autowired
    private FuncionarioSaudeService funcionarioSaudeService;

    // Buscar todos os funcionários
    @GetMapping
    public List<FuncionarioSaude> listar() {
        return funcionarioSaudeService.listarTodos();
    }

    // Inserir um novo funcionário
    @PostMapping
    public FuncionarioSaude inserir(@RequestBody FuncionarioSaude funcionarioSaude) {
        return funcionarioSaudeService.salvar(funcionarioSaude);
    }

    // Alterar parcialmente um funcionário
    @PatchMapping
    public FuncionarioSaude alterar(@RequestBody FuncionarioSaude funcionarioSaude) {
        return funcionarioSaudeService.salvar(funcionarioSaude);
    }

    // Alterar um funcionário por ID
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioSaude> alterar(@PathVariable Long id, @RequestBody FuncionarioSaude funcionarioSaude) {
        try {
            FuncionarioSaude funcionarioAtualizado = funcionarioSaudeService.atualizar(id, funcionarioSaude);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir um funcionário por ID
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        funcionarioSaudeService.deletar(id);
    }

    // Inserir vários funcionários de uma vez
    @PostMapping("/inserir-varios")
    public List<FuncionarioSaude> inserirVarios(@RequestBody List<FuncionarioSaude> funcionarios) {
        return funcionarioSaudeService.salvarTodos(funcionarios);
    }

    // Buscar um funcionário por ID
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioSaude> buscarPorId(@PathVariable Long id) {
        Optional<FuncionarioSaude> funcionario = funcionarioSaudeService.buscarPorId(id);
        return funcionario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar funcionários por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    public List<FuncionarioSaude> buscarPorNome(@PathVariable String nome) {
        return funcionarioSaudeService.buscarPorNome(nome);
    }

    // Buscar um funcionário por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    public FuncionarioSaude buscarPorCpf(@PathVariable String cpf) {
        return funcionarioSaudeService.buscarPorCpf(cpf);
    }
}