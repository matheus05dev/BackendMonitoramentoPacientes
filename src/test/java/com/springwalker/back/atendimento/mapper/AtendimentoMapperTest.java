package com.springwalker.back.atendimento.mapper;

import com.springwalker.back.atendimento.dto.AtendimentoRequestDTO;
import com.springwalker.back.atendimento.dto.AtendimentoResponseDTO;
import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.enums.StatusMonitoramento;
import com.springwalker.back.atendimento.enums.StatusPaciente;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.model.Quarto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class AtendimentoMapperTest {

    private AtendimentoMapper mapper = Mappers.getMapper(AtendimentoMapper.class);

    private Atendimento atendimento;
    private AtendimentoRequestDTO atendimentoRequestDTO;
    private Paciente paciente;
    private Quarto quarto;
    private FuncionarioSaude medicoResponsavel;
    private FuncionarioSaude medicoComplicacao;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder().id(1L).nome("João Silva").build();
        quarto = Quarto.builder().id(10L).numero(101).build();
        medicoResponsavel = FuncionarioSaude.builder().id(2L).nome("Dr. Carlos").build();
        medicoComplicacao = FuncionarioSaude.builder().id(3L).nome("Dr. Ana").build();


        atendimento = Atendimento.builder()
                .id(1L)
                .statusPaciente(StatusPaciente.Internado)
                .statusMonitoramento(StatusMonitoramento.MONITORANDO)
                .acompanhante("Maria Silva")
                .condicoesPreexistentes("Nenhuma")
                .diagnostico(Diagnostico.A41)
                .tratamento("Repouso e medicação")
                .dataEntrada(LocalDateTime.of(2023, 1, 15, 10, 0))
                .observacoes("Paciente com febre")
                .diagnostico_complicacao(Diagnostico.J12)
                .tratamento_complicacao("Antibióticos")
                .paciente(paciente)
                .quarto(quarto)
                .medicoResponsavel(medicoResponsavel)
                .medicoComplicacao(medicoComplicacao)
                .nomePaciente(paciente.getNome())
                .nomeMedicoResponsavel(medicoResponsavel.getNome())
                .nomeMedicoComplicacao(medicoComplicacao.getNome())
                .numeroQuarto(quarto.getNumero())
                .leituras(new ArrayList<>())
                .build();

        atendimentoRequestDTO = new AtendimentoRequestDTO();
        atendimentoRequestDTO.setPacienteId(paciente.getId());
        atendimentoRequestDTO.setMedicoResponsavelId(medicoResponsavel.getId());
        atendimentoRequestDTO.setMedicoComplicacaoId(medicoComplicacao.getId());
        atendimentoRequestDTO.setQuartoId(quarto.getId());
        atendimentoRequestDTO.setStatusPaciente(StatusPaciente.Internado);
        atendimentoRequestDTO.setStatusMonitoramento(StatusMonitoramento.MONITORANDO);
        atendimentoRequestDTO.setAcompanhante("Maria Silva");
        atendimentoRequestDTO.setCondicoesPreexistentes("Nenhuma");
        atendimentoRequestDTO.setDiagnostico(Diagnostico.A41);
        atendimentoRequestDTO.setTratamento("Repouso e medicação");
        atendimentoRequestDTO.setDataEntrada(LocalDateTime.of(2023, 1, 15, 10, 0));
        atendimentoRequestDTO.setObservacoes("Paciente com febre");
        atendimentoRequestDTO.setDiagnosticoComplicacao(Diagnostico.J12);
        atendimentoRequestDTO.setTratamentoComplicacao("Antibióticos");
    }

    @Test
    void toEntity() {
        Atendimento entity = mapper.toEntity(atendimentoRequestDTO);

        assertNotNull(entity);
        // Ignored fields should be null
        assertNull(entity.getPaciente());
        assertNull(entity.getMedicoResponsavel());
        assertNull(entity.getMedicoComplicacao());
        // Quarto is also ignored in the mapping, so it should be null
        assertNull(entity.getQuarto());


        assertEquals(atendimentoRequestDTO.getStatusPaciente(), entity.getStatusPaciente());
        assertEquals(atendimentoRequestDTO.getStatusMonitoramento(), entity.getStatusMonitoramento());
        assertEquals(atendimentoRequestDTO.getAcompanhante(), entity.getAcompanhante());
        assertEquals(atendimentoRequestDTO.getCondicoesPreexistentes(), entity.getCondicoesPreexistentes());
        assertEquals(atendimentoRequestDTO.getDiagnostico(), entity.getDiagnostico());
        assertEquals(atendimentoRequestDTO.getTratamento(), entity.getTratamento());
        assertEquals(atendimentoRequestDTO.getDataEntrada(), entity.getDataEntrada());
        assertEquals(atendimentoRequestDTO.getObservacoes(), entity.getObservacoes());
        assertEquals(atendimentoRequestDTO.getDiagnosticoComplicacao(), entity.getDiagnostico_complicacao());
        assertEquals(atendimentoRequestDTO.getTratamentoComplicacao(), entity.getTratamento_complicacao());
    }

    @Test
    void toResponseDTO() {
        AtendimentoResponseDTO dto = mapper.toResponseDTO(atendimento);

        assertNotNull(dto);
        assertEquals(atendimento.getId(), dto.getId());
        assertEquals(atendimento.getPaciente().getId(), dto.getPacienteId());
        assertEquals(atendimento.getMedicoResponsavel().getId(), dto.getMedicoResponsavelId());
        assertEquals(atendimento.getMedicoComplicacao().getId(), dto.getMedicoComplicacaoId());
        assertEquals(atendimento.getQuarto().getNumero(), dto.getNumeroQuarto());
        assertEquals(atendimento.getStatusPaciente(), dto.getStatusPaciente());
        assertEquals(atendimento.getStatusMonitoramento(), dto.getStatusMonitoramento());
        assertEquals(atendimento.getAcompanhante(), dto.getAcompanhante());
        assertEquals(atendimento.getCondicoesPreexistentes(), dto.getCondicoesPreexistentes());
        assertEquals(atendimento.getDiagnostico(), dto.getDiagnostico());
        assertEquals(atendimento.getTratamento(), dto.getTratamento());
        assertEquals(atendimento.getDataEntrada(), dto.getDataEntrada());
        assertEquals(atendimento.getObservacoes(), dto.getObservacoes());
        assertEquals(atendimento.getDiagnostico_complicacao(), dto.getDiagnosticoComplicacao());
        assertEquals(atendimento.getTratamento_complicacao(), dto.getTratamentoComplicacao());
        assertEquals(atendimento.getNomePaciente(), dto.getNomePaciente());
        assertEquals(atendimento.getNomeMedicoResponsavel(), dto.getNomeMedicoResponsavel());
        assertEquals(atendimento.getNomeMedicoComplicacao(), dto.getNomeMedicoComplicacao());
        assertTrue(dto.getLeituras().isEmpty());
    }
}
