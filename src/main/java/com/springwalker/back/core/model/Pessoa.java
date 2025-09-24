package com.springwalker.back.core.model;

import com.springwalker.back.core.enums.Sexo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Pessoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique=true)
    @EqualsAndHashCode.Include
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "Sexo")
    private Sexo sexo;

    @Temporal(TemporalType.DATE)
    @Basic
    @Column(name = "Data Nascimento")
    private LocalDate dataNascimento;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "Pessoa id")
    private List<Telefone> telefones = new ArrayList<Telefone>();

    @Column(name = "CPF", unique = true)
    private String cpf;

    public void addTelefone(Telefone telefone) {
        this.telefones.add(telefone);
    }

}
