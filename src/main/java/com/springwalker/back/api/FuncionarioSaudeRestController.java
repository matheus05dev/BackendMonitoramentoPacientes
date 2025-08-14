package com.springwalker.back.api;

import com.springwalker.back.model.FuncionarioSaude;
import com.springwalker.back.service.funcionarioSaude.AlteraFuncionarioSaudeService;
import com.springwalker.back.service.funcionarioSaude.BuscarFuncionarioSaudeService;
import com.springwalker.back.service.funcionarioSaude.CriaFuncionarioSaudeService;
import com.springwalker.back.service.funcionarioSaude.DelataFuncionarioSaudeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/funcionario")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FuncionarioSaudeRestController {

    private final BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;
    private final AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;
    private final CriaFuncionarioSaudeService criaFuncionarioSaudeService;
    private final DelataFuncionarioSaudeService delataFuncionarioSaudeService;

    // Buscar todos os funcionários
    @GetMapping
    public List<FuncionarioSaude> listar() {
        return buscarFuncionarioSaudeService.listarTodos();
    }

    // Inserir um novo funcionário
    @PostMapping
    public FuncionarioSaude inserir(@RequestBody FuncionarioSaude funcionarioSaude) {
        return criaFuncionarioSaudeService.salvar(funcionarioSaude);
    }

    // Alterar parcialmente um funcionário
    @PatchMapping
    public FuncionarioSaude alterar(@RequestBody FuncionarioSaude funcionarioSaude) {
        return criaFuncionarioSaudeService.salvar(funcionarioSaude);
    }

    // Alterar um funcionário por ID
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioSaude> alterar(@PathVariable Long id, @RequestBody FuncionarioSaude funcionarioSaude) {
        try {
            FuncionarioSaude funcionarioAtualizado = alteraFuncionarioSaudeService.atualizar(id, funcionarioSaude);
            return ResponseEntity.ok(funcionarioAtualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Excluir um funcionário por ID
    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        delataFuncionarioSaudeService.deletar(id);
    }

    // Inserir vários funcionários de uma vez
    @PostMapping("/inserir-varios")
    public List<FuncionarioSaude> inserirVarios(@RequestBody List<FuncionarioSaude> funcionarios) {
        return criaFuncionarioSaudeService.salvarTodos(funcionarios);
    }

    // Buscar um funcionário por ID
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioSaude> buscarPorId(@PathVariable Long id) {
        Optional<FuncionarioSaude> funcionario = buscarFuncionarioSaudeService.buscarPorId(id);
        return funcionario.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar funcionários por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    public List<FuncionarioSaude> buscarPorNome(@PathVariable String nome) {
        return buscarFuncionarioSaudeService.buscarPorNome(nome);
    }

    // Buscar um funcionário por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    public FuncionarioSaude buscarPorCpf(@PathVariable String cpf) {
        return buscarFuncionarioSaudeService.buscarPorCpf(cpf);
    }
}