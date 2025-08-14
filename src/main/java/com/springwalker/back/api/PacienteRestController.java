package com.springwalker.back.api;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.service.PacienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final PacienteService pacienteService;

    // Buscar todos os pacientes
    @GetMapping
    public List<Paciente> listar() {
        return pacienteService.listarTodos();
    }

    // Inserir um novo paciente
    @PostMapping
    public ResponseEntity<Paciente> inserir(@RequestBody Paciente paciente) {
        try {
            Paciente novoPaciente = pacienteService.inserir(paciente);
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
            return pacienteService.alterar(id, paciente);
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
            pacienteService.excluir(id);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir vários pacientes de uma vez
    @PostMapping("/inserir-varios")
    public List<Paciente> inserirVarios(@RequestBody List<Paciente> pacientes) {
        return pacienteService.inserirVarios(pacientes);
    }

    // Buscar um paciente por ID
    @GetMapping("/buscar/{id}")
    public Paciente buscarPorId(@PathVariable Long id) {
        try {
            return pacienteService.buscarPorId(id);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Buscar pacientes por nome (busca parcial)
    @GetMapping("/buscar-por-nome/{nome}")
    public List<Paciente> buscarPorNome(@PathVariable String nome) {
        return pacienteService.buscarPorNome(nome);
    }

    // Buscar pacientes por CPF
    @GetMapping("/buscar-por-cpf/{cpf}")
    public List<Paciente> buscarPorCpf(@PathVariable String cpf) {
        return pacienteService.buscarPorCpf(cpf);
    }
}