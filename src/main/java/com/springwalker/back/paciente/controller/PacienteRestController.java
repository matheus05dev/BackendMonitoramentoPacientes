package com.springwalker.back.paciente.controller;

import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.service.AlteraPacienteService;
import com.springwalker.back.paciente.service.BuscaPacienteService;
import com.springwalker.back.paciente.service.CriaPacienteService;
import com.springwalker.back.paciente.service.DeletaPacienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes") // Plural, seguindo o padrão REST
@RequiredArgsConstructor
public class PacienteRestController {

    private final CriaPacienteService criaPacienteService;
    private final BuscaPacienteService buscaPacienteService;
    private final AlteraPacienteService alteraPacienteService;
    private final DeletaPacienteService deletaPacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponseDTO> criarPaciente(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        try {
            PacienteResponseDTO responseDTO = criaPacienteService.execute(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            // Captura a exceção de CPF duplicado do serviço
            return ResponseEntity.badRequest().header("X-Error-Message", e.getMessage()).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PacienteResponseDTO>> listarTodosPacientes() {
        return ResponseEntity.ok(buscaPacienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(@PathVariable Long id) {
        return buscaPacienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorCpf(@PathVariable String cpf) {
        return buscaPacienteService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{nome}")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPacientePorNome(@RequestParam("nome") String nome) {
        return ResponseEntity.ok(buscaPacienteService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PacienteResponseDTO> alterarPaciente(@PathVariable Long id, @RequestBody @Valid PacienteRequestDTO requestDTO) {
        try {
            PacienteResponseDTO responseDTO = alteraPacienteService.execute(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        try {
            deletaPacienteService.execute(id);
            return ResponseEntity.noContent().build(); // Status 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
