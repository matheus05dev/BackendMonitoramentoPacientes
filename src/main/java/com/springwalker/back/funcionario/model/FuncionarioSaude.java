package com.springwalker.back.funcionario.model;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.core.enums.Cargo;
import com.springwalker.back.core.model.Pessoa;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioSaude extends Pessoa {

    @Enumerated(EnumType.STRING)
    @Column(name = "Cargo")
    private Cargo cargo;

    @ElementCollection
    @CollectionTable(name = "Especialidades", joinColumns = @JoinColumn(name = "Funcionario Id"))
    @Column(name = "Especialidade")
    private List<String> especialidades;

    @Column(unique = true, name = "Identificação" )
    private String identificacao;

    @OneToMany(mappedBy = "medicoResponsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atendimento> atendimentos;

    @OneToMany(mappedBy = "medicoComplicacao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Atendimento> atendimentosComplicacao;
}
