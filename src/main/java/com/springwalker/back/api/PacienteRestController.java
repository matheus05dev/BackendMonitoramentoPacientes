package com.springwalker.back.api;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.service.paciente.AlteraPacienteService;
import com.springwalker.back.service.paciente.BuscaPacienteService;
import com.springwalker.back.service.paciente.CriaPacienteService;
import com.springwalker.back.service.paciente.DeletaPacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/paciente")
@RequiredArgsConstructor
public class PacienteRestController {

    private final BuscaPacienteService buscaPacienteService;
    private final AlteraPacienteService alteraPacienteService;
    private final CriaPacienteService criaPacienteService;
    private final DeletaPacienteService deletaPacienteService;

    // Buscar todos os pacientes
    @GetMapping
    public List<Paciente> listar() {
        return buscaPacienteService.listarTodos();
    }

    // Inserir um novo paciente
    @PostMapping
    public ResponseEntity<Paciente> inserir(@RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = criaPacienteService.inserir(paciente);
            return new ResponseEntity<>(novoPaciente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Alterar um paciente por ID
    @PutMapping("/{id}")
    public Paciente alterar(@PathVariable Long id, @RequestBody Paciente paciente) {
        try {
            return alteraPacienteService.alterar(id, paciente);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Excluir um paciente por ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        try {
            deletaPacienteService.excluir(id);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir vários pacientes de uma vez
    @PostMapping("/inserir-varios")
    public List<Paciente> inserirVarios(@RequestBody List<Paciente> pacientes) {
        return criaPacienteService.inserirVarios(pacientes);
    }

    // Buscar um paciente por ID
    @GetMapping("/buscar/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        try {
            return buscaPacienteService.buscarPorId(id);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Buscar pacientes por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    public List<Paciente> buscarPorNome(@PathVariable String nome) {
        return buscaPacienteService.buscarPorNome(nome);
    }

    // Buscar pacientes por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    public List<Paciente> buscarPorCpf(@PathVariable String cpf) {
        return buscaPacienteService.buscarPorCpf(cpf);
    }
}