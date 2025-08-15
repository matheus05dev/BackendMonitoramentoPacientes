package com.springwalker.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.sql.Date;
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
    private Quarto quarto;
}
