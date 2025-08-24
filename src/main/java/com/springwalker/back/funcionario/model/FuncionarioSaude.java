package com.springwalker.back.funcionario.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.core.enums.Cargo;
import com.springwalker.back.core.model.Pessoa;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Entity
@SuperBuilder
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioSaude extends Pessoa {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Cargo")
    private Cargo cargo;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "Especialidades", joinColumns = @JoinColumn(name = "Funcionario Id"))
    @Column(name = "Especialidade")
    private List<String> especialidades;

    @NotEmpty
    @Column(unique = true, name = "Identificação" )
    private String identificacao;

    @OneToMany(mappedBy = "medicoResponsavel", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Atendimento> atendimentos;


    @OneToMany(mappedBy = "medicoComplicacao", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Atendimento> atendimentosComplicacao;
}
