package com.springwalker.back.paciente.model;

import com.springwalker.back.pessoa.model.Pessoa;
import com.springwalker.back.quarto.model.Quarto;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@SuperBuilder
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Paciente extends Pessoa {

    @ElementCollection
    @CollectionTable(name = "Alergias", joinColumns = @JoinColumn(name = "Paciente Id"))
    @Column(name = "Alergia")
    private List<String> alergias = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "Quarto_Id")
    private Quarto quarto;
}
