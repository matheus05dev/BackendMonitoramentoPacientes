package com.springwalker.back.paciente.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.springwalker.back.core.model.Pessoa;
import com.springwalker.back.quarto.model.Quarto;
import jakarta.persistence.*;
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
public class Paciente extends Pessoa {

    @ElementCollection
    @CollectionTable(name = "Alergias", joinColumns = @JoinColumn(name = "Paciente Id"))
    @Column(name = "Alergia")
    private List<String> alergias;

    @ManyToOne
    @JoinColumn(name = "Quarto Id")
    @JsonIgnore
    private Quarto quarto;
}
