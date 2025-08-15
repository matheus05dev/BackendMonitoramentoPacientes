package com.springwalker.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
import java.util.List;

@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "P")
public class Paciente extends Pessoa {

    @ElementCollection
    @CollectionTable(name = "Alergias", joinColumns = @JoinColumn(name = "Paciente Id"))
    @Column(name = "Alergia")
    private List<String> alergias;

    @ManyToOne
    @JoinColumn(name = "Quarto Id")
    private Quarto quarto;
}
