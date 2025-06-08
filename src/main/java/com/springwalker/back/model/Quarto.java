package com.springwalker.back.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Enumerated;
import com.springwalker.back.enums.LocalizacaoQuarto;
import com.springwalker.back.enums.TipoQuarto;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quarto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @NotEmpty
    @Column(name = "número", unique = true)
    private Integer numero;

    @NotEmpty
    @Enumerated
    @Column(name = "setor")
    private LocalizacaoQuarto localizacao;

    @NotEmpty
    @Enumerated
    @Column(name = "tipo")
    private TipoQuarto tipo;

    @NotEmpty
    @Column(name = "capacidade", unique = true)
    private Integer capacidade;
}
