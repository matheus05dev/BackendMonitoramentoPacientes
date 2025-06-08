package com.springwalker.back.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorValue(value = "P")
public class Paciente extends Pessoa {

    @NotEmpty
    @Column
    private String quadro;

    @NotEmpty
    @Column
    private String historico;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "alergias", joinColumns = @JoinColumn(name = "paciente id"))
    @Column(name = "alergia")
    private List<String> alergias;

    @NotEmpty
    @ManyToOne
    @JoinColumn(name = "medico responsavel id")
    private FuncionarioSaude medicoResponsavel;

    @NotEmpty
    @ElementCollection
    @CollectionTable(name = "condições preexistentes", joinColumns = @JoinColumn(name = "paciente id"))
    @Column(name = "condições preexistentes")
    private List<String> condicoes_preexistentes;

    @NotEmpty
    @Column(name = "leito")
    private String leito;

    @ManyToOne
    @JoinColumn(name = "quarto id")
    private Quarto quarto;

    @NotEmpty
    @Temporal(TemporalType.DATE)
    private Date dataInternacao;
}
