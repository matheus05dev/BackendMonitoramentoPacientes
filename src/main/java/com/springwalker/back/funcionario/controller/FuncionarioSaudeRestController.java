package com.springwalker.back.funcionario.controller;

import com.springwalker.back.funcionario.dto.FuncionarioSaudeRequestDTO;
import com.springwalker.back.funcionario.dto.FuncionarioSaudeResponseDTO;
import com.springwalker.back.funcionario.service.AlteraFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.BuscarFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.CriaFuncionarioSaudeService;
import com.springwalker.back.funcionario.service.DelataFuncionarioSaudeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/funcionarios") // Plural, seguindo o padrão REST
@RequiredArgsConstructor
public class FuncionarioSaudeRestController {

    private final CriaFuncionarioSaudeService criaFuncionarioSaudeService;
    private final BuscarFuncionarioSaudeService buscarFuncionarioSaudeService;
    private final AlteraFuncionarioSaudeService alteraFuncionarioSaudeService;
    private final DelataFuncionarioSaudeService delataFuncionarioSaudeService;

    @PostMapping
    public ResponseEntity<FuncionarioSaudeResponseDTO> criarFuncionario(@RequestBody @Valid FuncionarioSaudeRequestDTO requestDTO) {
        FuncionarioSaudeResponseDTO responseDTO = criaFuncionarioSaudeService.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioSaudeResponseDTO>> listarTodosFuncionarios() {
        return ResponseEntity.ok(buscarFuncionarioSaudeService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        return buscarFuncionarioSaudeService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{cpf}") // Adicionado um caminho específico para busca por CPF
    public ResponseEntity<FuncionarioSaudeResponseDTO> buscarFuncionarioPorCpf(@PathVariable String cpf) {
        return buscarFuncionarioSaudeService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{nome}")
    public ResponseEntity<List<FuncionarioSaudeResponseDTO>> buscarFuncionarioPorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(buscarFuncionarioSaudeService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioSaudeResponseDTO> alterarFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioSaudeRequestDTO requestDTO) {
        try {
            FuncionarioSaudeResponseDTO responseDTO = alteraFuncionarioSaudeService.execute(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            // Idealmente, capturar uma exceção mais específica, como EntityNotFoundException
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        try {
            delataFuncionarioSaudeService.execute(id);
            return ResponseEntity.noContent().build(); // Status 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
