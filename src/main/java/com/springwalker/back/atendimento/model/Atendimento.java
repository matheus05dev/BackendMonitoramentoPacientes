package com.springwalker.back.atendimento.model;

import com.springwalker.back.core.enums.Diagnostico;
import com.springwalker.back.core.enums.StatusPaciente;
import com.springwalker.back.funcionario.model.FuncionarioSaude;
import com.springwalker.back.paciente.model.Paciente;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status Paciente")
    private StatusPaciente statusPaciente;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Paciente Id")
    private Paciente paciente;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "Médico Responsável Id")
    private FuncionarioSaude medicoResponsavel;

    @ManyToOne
    @JoinColumn(name = "Medico Complicacao Id")
    private FuncionarioSaude medicoComplicacao;


}
