package com.springwalker.back.atendimento.model;

import com.springwalker.back.core.enums.Diagnostico;
import com.springwalker.back.core.enums.StatusMonitoramento;
import com.springwalker.back.core.enums.StatusPaciente;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.paciente.model.Paciente;
import com.springwalker.back.quarto.model.Quarto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder; // Adicionado

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status Paciente")
    private StatusPaciente statusPaciente;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_monitoramento")
    private StatusMonitoramento statusMonitoramento;

    @Column(name = "Acompanhante")
    private String acompanhante;

    @Column(name = "Condições preexistentes")
    private String condicoesPreexistentes;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Diagnóstico Principal")
    private Diagnostico diagnostico;

    @NotNull
    @Column(name = "Tratamento Principal")
    private String tratamento;

    @NotNull
    @Column(name = "Data entrada")
    private LocalDateTime dataEntrada;

    @Column(name = "Data saída")
    private LocalDateTime dataSaida;

    @Column(name = "Observações")
    private String observacoes;

    @Enumerated(EnumType.STRING)
    @Column(name = "Diagnóstico_Complicação")
    private Diagnostico diagnostico_complicacao;

    @Column(name = "Tratamento complicação")
    private String tratamento_complicacao;

    @ManyToOne
    @JoinColumn(name = "Paciente Id", nullable = true)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "quarto_id")
    private Quarto quarto;

    @ManyToOne
    @JoinColumn(name = "Médico Responsável Id", nullable = true)
    private FuncionarioSaude medicoResponsavel;

    @ManyToOne
    @JoinColumn(name = "Medico Complicacao Id")
    private FuncionarioSaude medicoComplicacao;

    @Column(name = "Nome Paciente", updatable = false)
    private String nomePaciente;

    @Column(name = "Nome Médico Responsável", updatable = false)
    private String nomeMedicoResponsavel;

    @Column(name = "Nome Médico Complicação", updatable = false)
    private String nomeMedicoComplicacao;

    @Column(name = "Numero Quarto")
    private Integer numeroQuarto;
    
    @OneToMany(mappedBy = "atendimento", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeituraSensor> leituras;
}
