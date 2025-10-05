package com.springwalker.back.paciente.controller;

import com.springwalker.back.paciente.dto.PacienteRequestDTO;
import com.springwalker.back.paciente.dto.PacienteResponseDTO;
import com.springwalker.back.paciente.service.AlteraPacienteService;
import com.springwalker.back.paciente.service.BuscaPacienteService;
import com.springwalker.back.paciente.service.CriaPacienteService;
import com.springwalker.back.paciente.service.DeletaPacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes") // Plural, seguindo o padrão REST
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public class PacienteRestController {

    private final CriaPacienteService criaPacienteService;
    private final BuscaPacienteService buscaPacienteService;
    private final AlteraPacienteService alteraPacienteService;
    private final DeletaPacienteService deletaPacienteService;

    @PostMapping
    @Operation(summary = "Criar novo paciente", description = "Cria um novo paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<?> criarPaciente(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        try {
            PacienteResponseDTO responseDTO = criaPacienteService.execute(requestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (IllegalArgumentException e) {
            // Captura a exceção de CPF duplicado do serviço
            Map<String, String> error = new HashMap<>();
            error.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping
    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponseDTO>> listarTodosPacientes() {
        return ResponseEntity.ok(buscaPacienteService.listarTodos());
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna os detalhes de um paciente específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(@PathVariable Long id) {
        return buscaPacienteService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cpf/{cpf}")
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna os detalhes de um paciente específico pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorCpf(@PathVariable String cpf) {
        return buscaPacienteService.buscarPorCpf(cpf)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nome/{nome}")
    @Operation(summary = "Buscar pacientes por nome", description = "Retorna uma lista de pacientes que correspondem ao nome informado")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPacientePorNome(@PathVariable String nome) {
        return ResponseEntity.ok(buscaPacienteService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar paciente", description = "Atualiza os dados de um paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> alterarPaciente(@PathVariable Long id,
            @RequestBody @Valid PacienteRequestDTO requestDTO) {
        try {
            PacienteResponseDTO responseDTO = alteraPacienteService.execute(id, requestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar paciente", description = "Remove um paciente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        try {
            deletaPacienteService.execute(id);
            return ResponseEntity.noContent().build(); // Status 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
