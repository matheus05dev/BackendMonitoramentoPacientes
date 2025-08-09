package com.springwalker.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "P")
public class Paciente extends Pessoa {


    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "alergias", joinColumns = @JoinColumn(name = "paciente id"))
    @Column(name = "alergia")
    private List<String> alergias;

    @ManyToOne
    @JoinColumn(name = "quarto_id") // Esta é a coluna de chave estrangeira
    private Quarto quarto;
}
