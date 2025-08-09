package com.springwalker.back.api;

import com.springwalker.back.model.Quarto;
import com.springwalker.back.service.QuartoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/quarto")
@CrossOrigin(origins = "*")
public class QuartoRestController {

    @Autowired
    private QuartoService quartoService;

    // Buscar todos os quartos
    @GetMapping
    public List<Quarto> listar() {
        return quartoService.listarTodos();
    }

    // Buscar um quarto por ID
    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable Long id) {
        try {
            return quartoService.buscarPorId(id);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir um novo quarto
    @PostMapping
    public ResponseEntity<Quarto> inserir(@RequestBody Quarto quarto) {
        Quarto novoQuarto = quartoService.inserir(quarto);
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    // Alterar um quarto
    @PutMapping("/{id}")
    public Quarto alterar(@PathVariable Long id, @RequestBody Quarto quarto) {
        try {
            return quartoService.alterar(id, quarto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Apagar um quarto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        try {
            quartoService.excluir(id);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir vários quartos
    @PostMapping("/inserir-varios")
    public List<Quarto> inserirVarios(@RequestBody List<Quarto> quartos) {
        return quartoService.inserirVarios(quartos);
    }

    // Endpoint para alocar paciente em um quarto
    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    public Quarto alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        try {
            return quartoService.alocarPaciente(quartoId, pacienteId);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Endpoint para remover paciente de um quarto
    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    public Quarto removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        try {
            return quartoService.removerPaciente(quartoId, pacienteId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}