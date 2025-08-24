package com.springwalker.back.quarto.controller;

import com.springwalker.back.quarto.model.Quarto;
import com.springwalker.back.quarto.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/quarto")
@RequiredArgsConstructor
public class QuartoRestController {

    private final BuscaQuartoService buscaQuartoService;
    private final CriaQuartoService criaQuartoService;
    private final DeletaQuartoService deletaQuartoService;
    private final AlteraQuartoService alteraQuartoService;
    private final AtribuiPacienteQuartoService atribuiPacienteQuartoService;
    private final RemovePacienteQuartoService removePacienteQuartoService;

    // Buscar todos os quartos
    @GetMapping
    public List<Quarto> listar() {
        return buscaQuartoService.listarTodos();
    }

    // Buscar um quarto por ID
    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable Long id) {
        try {
            return buscaQuartoService.buscarPorId(id);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir um novo quarto
    @PostMapping
    public ResponseEntity<Quarto> inserir(@RequestBody Quarto quarto) {
        Quarto novoQuarto = criaQuartoService.inserir(quarto);
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    // Alterar um quarto
    @PutMapping("/{id}")
    public Quarto alterar(@PathVariable Long id, @RequestBody Quarto quarto) {
        try {
            return alteraQuartoService.alterar(id, quarto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Apagar um quarto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        try {
            deletaQuartoService.excluir(id);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    // Inserir vários quartos
    @PostMapping("/inserir-varios")
    public List<Quarto> inserirVarios(@RequestBody List<Quarto> quartos) {
        return criaQuartoService.inserirVarios(quartos);
    }

    // Endpoint para alocar paciente em um quarto
    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    public Quarto alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        try {
            return atribuiPacienteQuartoService.alocarPaciente(quartoId, pacienteId);
        } catch (IllegalStateException e) {
            // A exceção da lógica de negócio é tratada aqui
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Endpoint para remover paciente de um quarto
    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    public Quarto removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        try {
            return removePacienteQuartoService.removerPaciente(quartoId, pacienteId);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}