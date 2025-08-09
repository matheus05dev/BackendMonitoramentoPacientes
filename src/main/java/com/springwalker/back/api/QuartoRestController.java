package com.springwalker.back.api;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.PacienteRepository;
import com.springwalker.back.repository.QuartoRepository;
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
    private QuartoRepository quartoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    // Buscar todos os quartos
    @GetMapping
    public List<Quarto> listar() {
        return quartoRepository.findAll();
    }

    // Buscar um quarto por ID
    @GetMapping("/{id}")
    public Quarto buscarPorId(@PathVariable Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + id));
    }

    // Inserir um novo quarto
    @PostMapping
    public ResponseEntity<Quarto> inserir(@RequestBody Quarto quarto) {
        Quarto novoQuarto = quartoRepository.save(quarto);
        return new ResponseEntity<>(novoQuarto, HttpStatus.CREATED);
    }

    // Alterar um quarto
    @PutMapping("/{id}")
    public Quarto alterar(@PathVariable Long id, @RequestBody Quarto quarto) {
        Quarto quartoExistente = quartoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + id));

        if (quarto.getCapacidade() != null) {
            quartoExistente.setCapacidade(quarto.getCapacidade());
        }
        if (quarto.getLocalizacao() != null) {
            quartoExistente.setLocalizacao(quarto.getLocalizacao());
        }
        if (quarto.getNumero() != null) {
            quartoExistente.setNumero(quarto.getNumero());
        }
        if (quarto.getTipo() != null) {
            quartoExistente.setTipo(quarto.getTipo());
        }

        return quartoRepository.save(quartoExistente);
    }

    // Apagar um quarto
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + id);
        }
        quartoRepository.deleteById(id);
    }

    // Inserir vários quartos
    @PostMapping("/inserir-varios")
    public List<Quarto> inserirVarios(@RequestBody List<Quarto> quartos) {
        return quartoRepository.saveAll(quartos);
    }

    // Novo endpoint para alocar paciente em um quarto
    @PutMapping("/{quartoId}/alocar-paciente/{pacienteId}")
    public Quarto alocarPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + pacienteId));

        try {
            // A lógica de negócio está no modelo, então a chamamos aqui
            quarto.adicionarPaciente(paciente);

            // Atualiza a referência do quarto no paciente
            paciente.setQuarto(quarto);
            pacienteRepository.save(paciente);

            // Salva as alterações no quarto
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // Novo endpoint para remover paciente de um quarto
    @PutMapping("/{quartoId}/remover-paciente/{pacienteId}")
    public Quarto removerPaciente(@PathVariable Long quartoId, @PathVariable Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente não encontrado com o ID: " + pacienteId));

        try {
            // A lógica de negócio está no modelo, então a chamamos aqui
            quarto.removerPaciente(paciente);

            // Remove a referência do quarto do paciente
            paciente.setQuarto(null);
            pacienteRepository.save(paciente);

            // Salva as alterações no quarto
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}