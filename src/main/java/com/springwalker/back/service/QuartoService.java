package com.springwalker.back.service;

import com.springwalker.back.model.Paciente;
import com.springwalker.back.model.Quarto;
import com.springwalker.back.repository.PacienteRepository;
import com.springwalker.back.repository.QuartoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuartoService {

    private final QuartoRepository quartoRepository;

    private final PacienteRepository pacienteRepository;

    // Lógica para buscar todos os quartos
    public List<Quarto> listarTodos() {
        return quartoRepository.findAll();
    }

    // Lógica para buscar um quarto por ID
    public Quarto buscarPorId(Long id) {
        return quartoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));
    }

    // Lógica para inserir um novo quarto
    public Quarto inserir(Quarto quarto) {
        return quartoRepository.save(quarto);
    }

    // Lógica para alterar um quarto
    public Quarto alterar(Long id, Quarto quarto) {
        Quarto quartoExistente = quartoRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + id));

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

    // Lógica para apagar um quarto
    public void excluir(Long id) {
        if (!quartoRepository.existsById(id)) {
            throw new IllegalStateException("Quarto não encontrado com o ID: " + id);
        }
        quartoRepository.deleteById(id);
    }

    // Lógica para inserir vários quartos
    public List<Quarto> inserirVarios(List<Quarto> quartos) {
        return quartoRepository.saveAll(quartos);
    }

    // Lógica de negócio para alocar paciente em um quarto
    @Transactional // Garante que as operações em paciente e quarto ocorram em uma única transação
    public Quarto alocarPaciente(Long quartoId, Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalStateException("Paciente não encontrado com o ID: " + pacienteId));

        try {
            quarto.adicionarPaciente(paciente);
            paciente.setQuarto(quarto);
            pacienteRepository.save(paciente);
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    // Lógica de negócio para remover paciente de um quarto
    @Transactional
    public Quarto removerPaciente(Long quartoId, Long pacienteId) {
        Quarto quarto = quartoRepository.findById(quartoId)
                .orElseThrow(() -> new IllegalStateException("Quarto não encontrado com o ID: " + quartoId));
        Paciente paciente = pacienteRepository.findById(pacienteId)
                .orElseThrow(() -> new IllegalStateException("Paciente não encontrado com o ID: " + pacienteId));

        try {
            quarto.removerPaciente(paciente);
            paciente.setQuarto(null);
            pacienteRepository.save(paciente);
            return quartoRepository.save(quarto);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}