package com.springwalker.back.model;

import com.springwalker.back.enums.Cargo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@DiscriminatorValue(value = "F")
@Entity
@SuperBuilder
@Data
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
}
