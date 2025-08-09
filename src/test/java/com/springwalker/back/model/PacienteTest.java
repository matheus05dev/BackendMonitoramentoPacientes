package com.springwalker.back.model;

import com.springwalker.back.enums.Sexo;
import com.springwalker.back.repository.PacienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PacienteTest {
    
    @Autowired
    private PacienteRepository pacienteRepository;

    @Test
    void inserir() {
        Paciente paciente = new Paciente();
        paciente.setCpf("453.842.298-37");
        paciente.setSexo(Sexo.Femenino);
        paciente.setEmail("m@v.com");
        paciente.setDataNascimento(Date.valueOf(LocalDate.of(2025,12,15)).toLocalDate());
        paciente.setNome("Joana Pereira");
        paciente.setAlergias(Collections.singletonList("Nenhuma"));
        pacienteRepository.save(paciente);
    }

    @Test
    void alterar() {
        Paciente paciente = pacienteRepository.findById(32l).orElse(null);
        assertNotNull(paciente);
        paciente.setNome("Maria Eduarda Meira Camargo");
        pacienteRepository.save(paciente);
    }

    @Test
    void excluir() {
        Paciente paciente = pacienteRepository.findById(32L).orElse(null);
        assertNotNull(paciente);
        paciente.setNome("Maria Eduarda Meira Camargo");
        pacienteRepository.delete(paciente);
    }

}