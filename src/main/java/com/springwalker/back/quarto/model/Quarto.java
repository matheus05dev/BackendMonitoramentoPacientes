package com.springwalker.back.quarto.model;

import com.springwalker.back.paciente.model.Paciente;
import jakarta.persistence.*;
import com.springwalker.back.quarto.enums.LocalizacaoQuarto;
import com.springwalker.back.quarto.enums.TipoQuarto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;


@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Quarto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "numero", unique = true)
    private Integer numero;

    @Enumerated(EnumType.STRING)
    @Column(name = "localizacao")
    private LocalizacaoQuarto localizacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo")
    private TipoQuarto tipo;

    @Column(name = "capacidade")
    private Integer capacidade;

    @OneToMany(mappedBy = "quarto")
    @Builder.Default
    private List<Paciente> pacientes = new ArrayList<>();

     // Adiciona um paciente ao quarto, respeitando a capacidade e evitando duplicidade.

    public void adicionarPaciente(Paciente paciente) {
        if (pacientes.contains(paciente)) {
            throw new IllegalStateException(
                    "Paciente já está neste quarto."
            );
        }

        if (pacientes.size() >= capacidade) {
            throw new IllegalStateException(
                    "Quarto " + numero + " já atingiu a capacidade máxima de " + capacidade + " pacientes."
            );
        }

        pacientes.add(paciente);
    }

    // Remove um paciente do quarto.

    public void removerPaciente(Paciente paciente) {
        if (!pacientes.remove(paciente)) {
            throw new IllegalStateException(
                    "Paciente não encontrado no quarto " + numero
            );
        }
    }


  // Verifica se ainda há vagas no quarto.

    public boolean temVaga() {
        return pacientes.size() < capacidade;
    }
}
