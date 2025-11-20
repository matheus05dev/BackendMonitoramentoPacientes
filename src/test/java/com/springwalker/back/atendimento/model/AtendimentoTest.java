package com.springwalker.back.atendimento.model;

import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.enums.StatusMonitoramento;
import com.springwalker.back.atendimento.enums.StatusPaciente;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AtendimentoTest {

    @Test
    void testAtendimentoBuilder() {
        Paciente paciente = Paciente.builder().id(1L).nome("João").build();
        Quarto quarto = Quarto.builder().id(1L).numero(101).build();
        FuncionarioSaude medico = FuncionarioSaude.builder().id(1L).nome("Dr. Silva").build();

        Atendimento atendimento = Atendimento.builder()
                .id(1L)
                .statusPaciente(StatusPaciente.Internado)
                .statusMonitoramento(StatusMonitoramento.MONITORANDO)
                .acompanhante("Maria")
                .condicoesPreexistentes("Diabetes")
                .diagnostico(Diagnostico.A41)
                .tratamento("Medicação")
                .dataEntrada(LocalDateTime.now())
                .observacoes("Paciente responsivo")
                .paciente(paciente)
                .quarto(quarto)
                .medicoResponsavel(medico)
                .leituras(new ArrayList<>())
                .nomePaciente(paciente.getNome())
                .nomeMedicoResponsavel(medico.getNome())
                .numeroQuarto(quarto.getNumero())
                .build();

        assertNotNull(atendimento);
        assertEquals(1L, atendimento.getId());
        assertEquals(StatusPaciente.Internado, atendimento.getStatusPaciente());
        assertEquals(StatusMonitoramento.MONITORANDO, atendimento.getStatusMonitoramento());
        assertEquals("Maria", atendimento.getAcompanhante());
        assertEquals("Diabetes", atendimento.getCondicoesPreexistentes());
        assertEquals(Diagnostico.A41, atendimento.getDiagnostico());
        assertEquals("Medicação", atendimento.getTratamento());
        assertNotNull(atendimento.getDataEntrada());
        assertEquals("Paciente responsivo", atendimento.getObservacoes());
        assertEquals(paciente, atendimento.getPaciente());
        assertEquals(quarto, atendimento.getQuarto());
        assertEquals(medico, atendimento.getMedicoResponsavel());
        assertNotNull(atendimento.getLeituras());
        assertEquals("João", atendimento.getNomePaciente());
        assertEquals("Dr. Silva", atendimento.getNomeMedicoResponsavel());
        assertEquals(101, atendimento.getNumeroQuarto());
    }

    @Test
    void testAtendimentoSettersAndGetters() {
        Atendimento atendimento = new Atendimento();
        Long id = 2L;
        StatusPaciente statusPaciente = StatusPaciente.Liberado;
        StatusMonitoramento statusMonitoramento = StatusMonitoramento.NAO_MONITORADO;
        String acompanhante = "Pedro";
        String condicoesPreexistentes = "Asma";
        Diagnostico diagnostico = Diagnostico.J15;
        String tratamento = "Insulina";
        LocalDateTime dataEntrada = LocalDateTime.now().minusDays(1);
        LocalDateTime dataSaida = LocalDateTime.now();
        String observacoes = "Melhora progressiva";
        Diagnostico diagnosticoComplicacao = Diagnostico.J12;
        String tratamentoComplicacao = "Antibióticos";
        Paciente paciente = Paciente.builder().id(2L).nome("Maria").build();
        Quarto quarto = Quarto.builder().id(2L).numero(102).build();
        FuncionarioSaude medicoResponsavel = FuncionarioSaude.builder().id(2L).nome("Dr. Souza").build();
        FuncionarioSaude medicoComplicacao = FuncionarioSaude.builder().id(3L).nome("Dr. Costa").build();
        String nomePaciente = "Maria";
        String nomeMedicoResponsavel = "Dr. Souza";
        String nomeMedicoComplicacao = "Dr. Costa";
        Integer numeroQuarto = 102;

        atendimento.setId(id);
        atendimento.setStatusPaciente(statusPaciente);
        atendimento.setStatusMonitoramento(statusMonitoramento);
        atendimento.setAcompanhante(acompanhante);
        atendimento.setCondicoesPreexistentes(condicoesPreexistentes);
        atendimento.setDiagnostico(diagnostico);
        atendimento.setTratamento(tratamento);
        atendimento.setDataEntrada(dataEntrada);
        atendimento.setDataSaida(dataSaida);
        atendimento.setObservacoes(observacoes);
        atendimento.setDiagnostico_complicacao(diagnosticoComplicacao);
        atendimento.setTratamento_complicacao(tratamentoComplicacao);
        atendimento.setPaciente(paciente);
        atendimento.setQuarto(quarto);
        atendimento.setMedicoResponsavel(medicoResponsavel);
        atendimento.setMedicoComplicacao(medicoComplicacao);
        atendimento.setNomePaciente(nomePaciente);
        atendimento.setNomeMedicoResponsavel(nomeMedicoResponsavel);
        atendimento.setNomeMedicoComplicacao(nomeMedicoComplicacao);
        atendimento.setNumeroQuarto(numeroQuarto);

        assertEquals(id, atendimento.getId());
        assertEquals(statusPaciente, atendimento.getStatusPaciente());
        assertEquals(statusMonitoramento, atendimento.getStatusMonitoramento());
        assertEquals(acompanhante, atendimento.getAcompanhante());
        assertEquals(condicoesPreexistentes, atendimento.getCondicoesPreexistentes());
        assertEquals(diagnostico, atendimento.getDiagnostico());
        assertEquals(tratamento, atendimento.getTratamento());
        assertEquals(dataEntrada, atendimento.getDataEntrada());
        assertEquals(dataSaida, atendimento.getDataSaida());
        assertEquals(observacoes, atendimento.getObservacoes());
        assertEquals(diagnosticoComplicacao, atendimento.getDiagnostico_complicacao());
        assertEquals(tratamentoComplicacao, atendimento.getTratamento_complicacao());
        assertEquals(paciente, atendimento.getPaciente());
        assertEquals(quarto, atendimento.getQuarto());
        assertEquals(medicoResponsavel, atendimento.getMedicoResponsavel());
        assertEquals(medicoComplicacao, atendimento.getMedicoComplicacao());
        assertEquals(nomePaciente, atendimento.getNomePaciente());
        assertEquals(nomeMedicoResponsavel, atendimento.getNomeMedicoResponsavel());
        assertEquals(nomeMedicoComplicacao, atendimento.getNomeMedicoComplicacao());
        assertEquals(numeroQuarto, atendimento.getNumeroQuarto());
    }
}
