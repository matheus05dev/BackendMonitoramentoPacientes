package com.springwalker.back.model;

import com.springwalker.back.enums.Cargo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@DiscriminatorValue(value = "F")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioSaude extends Pessoa {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cargo")
    private Cargo cargo;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "especialidades", joinColumns = @JoinColumn(name = "funcionario_id"))
    @Column(name = "especialidade")
    private List<String> especialidades;

    @NotEmpty
    @Column(unique = true)
    private String indentificacao;
}
