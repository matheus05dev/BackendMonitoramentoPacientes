package com.springwalker.back.atendimento.controller;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.service.AlteraAtendimentoService;
import com.springwalker.back.atendimento.service.BuscaAtendimentoService;
import com.springwalker.back.atendimento.service.CriaAtendimentoService;
import com.springwalker.back.atendimento.service.DeletaAtendimentoService;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.funcionario.repository.FuncionarioSaudeRepository;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.paciente.repository.PacienteRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/atendimento")
public class AtendimentoRestController {
    private final AlteraAtendimentoService alteraAtendimento;
    private final CriaAtendimentoService criaAtendimento;
    private final BuscaAtendimentoService buscaAtendimento;
    private final DeletaAtendimentoService deletaAtendimento;
    private final PacienteRepository pacienteRepository;
    private final FuncionarioSaudeRepository funcionarioSaudeRepository;

    // Inserir um novo atendimento
    @PostMapping
    public ResponseEntity<AtendimentoResponseDTO> criaAtendimento(@Valid @RequestBody AtendimentoRequestDTO dto) {
        AtendimentoResponseDTO responseDTO = criaAtendimento.criarAtendimento(dto);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    // Buscar atendimento por Id
    @GetMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> buscarAtendimentoPorId(@PathVariable Long id) {
        return buscaAtendimento.buscarAtendimentoPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Buscar todos atendimentos
    @GetMapping
    public ResponseEntity<List<AtendimentoResponseDTO>> buscarTodosAtendimentos() {
        List<AtendimentoResponseDTO> dtos = buscaAtendimento.buscarTodosAtendimentos();
        return ResponseEntity.ok(dtos);
    }

    // Alterar um atendimento existente
    @PutMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> alterarAtendimento(@PathVariable Long id, @Valid @RequestBody AtendimentoRequestDTO dto) {
        AtendimentoResponseDTO responseDTO = alteraAtendimento.alterarAtendimento(id, dto);
        return ResponseEntity.ok(responseDTO);
    }

    // Apagar um atendimento
    @DeleteMapping("/{id}")
    public ResponseEntity<AtendimentoResponseDTO> deletarAtendimento(@PathVariable Long id) {
        AtendimentoResponseDTO dto = deletaAtendimento.deletarAtendimento(id);
        return ResponseEntity.ok(dto);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
