package com.springwalker.back.paciente.controller;

import com.springwalker.back.core.log.service.LogService; // Importar LogService
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
@Tag(name = "Pacientes", description = "Gerenciamento de pacientes")
public class PacienteRestController {

    private final CriaPacienteService criaPacienteService;
    private final BuscaPacienteService buscaPacienteService;
    private final AlteraPacienteService alteraPacienteService;
    private final DeletaPacienteService deletaPacienteService;
    private final LogService logService; // Injetar LogService

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO','ENFERMEIRO')")
    @Operation(summary = "Criar novo paciente", description = "Cria um novo paciente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paciente criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<PacienteResponseDTO> criarPaciente(@RequestBody @Valid PacienteRequestDTO requestDTO) {
        PacienteResponseDTO responseDTO = criaPacienteService.execute(requestDTO);
        logService.logEvent("CRIACAO_PACIENTE", "Criação de novo paciente: " + responseDTO.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Listar todos os pacientes", description = "Retorna uma lista de todos os pacientes cadastrados")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponseDTO>> listarTodosPacientes() {
        logService.logEvent("LISTAGEM_PACIENTES", "Listagem de todos os pacientes");
        return ResponseEntity.ok(buscaPacienteService.listarTodos());
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar paciente por ID", description = "Retorna os detalhes de um paciente específico pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorId(@PathVariable Long id) {
        logService.logEvent("BUSCA_PACIENTE", "Busca de paciente por ID: " + id);
        return ResponseEntity.ok(buscaPacienteService.buscarPorId(id)
                .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado com o ID: " + id)));
    }

    @GetMapping("/cpf/{cpf}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar paciente por CPF", description = "Retorna os detalhes de um paciente específico pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente encontrado"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> buscarPacientePorCpf(@PathVariable String cpf) {
        logService.logEvent("BUSCA_PACIENTE_CPF", "Busca de paciente por CPF: " + cpf);
        return ResponseEntity.ok(buscaPacienteService.buscarPorCpf(cpf)
                .orElseThrow(() -> new NoSuchElementException("Paciente não encontrado com o CPF: " + cpf)));
    }

    @GetMapping("/nome/{nome}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO', 'AUXILIAR_ENFERMAGEM', 'TECNICO_ENFERMAGEM', 'ESTAGIARIO')")
    @Operation(summary = "Buscar pacientes por nome", description = "Retorna uma lista de pacientes que correspondem ao nome informado")
    @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    public ResponseEntity<List<PacienteResponseDTO>> buscarPacientePorNome(@PathVariable String nome) {
        logService.logEvent("BUSCA_PACIENTE_NOME", "Busca de paciente por nome: " + nome);
        return ResponseEntity.ok(buscaPacienteService.buscarPorNome(nome));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'ENFERMEIRO')")
    @Operation(summary = "Alterar paciente", description = "Atualiza os dados de um paciente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paciente alterado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> alterarPaciente(@PathVariable Long id,
            @RequestBody @Valid PacienteRequestDTO requestDTO) {
        logService.logEvent("ALTERACAO_PACIENTE", "Alteração de paciente: " + id);
        PacienteResponseDTO responseDTO = alteraPacienteService.execute(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Deletar paciente", description = "Remove um paciente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paciente deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<Void> deletarPaciente(@PathVariable Long id) {
        logService.logEvent("EXCLUSAO_PACIENTE", "Exclusão de paciente: " + id);
        deletaPacienteService.execute(id);
        return ResponseEntity.noContent().build();
    }
}
