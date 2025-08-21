package com.springwalker.back.quarto.model;

import com.springwalker.back.paciente.model.Paciente;
import jakarta.persistence.*;
import com.springwalker.back.core.enums.LocalizacaoQuarto;
import com.springwalker.back.core.enums.TipoQuarto;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
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
    @Column(name = "id", unique = true)
    private Long id;

    @NotNull
    @Column(name = "Número", unique = true)
    private Integer numero;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Setor")
    private LocalizacaoQuarto localizacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "Tipo")
    private TipoQuarto tipo;

    @NotNull
    @Column(name = "Capacidade")
    private Integer capacidade;

    @OneToMany(mappedBy = "quarto")
    private List<Paciente> pacientes = new ArrayList<>();

    /**
     * Adiciona um paciente ao quarto, respeitando a capacidade e evitando duplicidade.
     */
    public void adicionarPaciente(Paciente paciente) {
        if (pacientes == null) {
            pacientes = new ArrayList<>();
        }

        if (pacientes.size() >= capacidade) {
            throw new IllegalStateException(
                    "Quarto " + numero + " já atingiu a capacidade máxima de " + capacidade + " pacientes."
            );
        }

        if (pacientes.contains(paciente)) {
            throw new IllegalStateException(
                    "Paciente já está neste quarto."
            );
        }

        pacientes.add(paciente);
    }

    /**
     * Remove um paciente do quarto.
     */
    public void removerPaciente(Paciente paciente) {
        if (!pacientes.remove(paciente)) {
            throw new IllegalStateException(
                    "Paciente não encontrado no quarto " + numero
            );
        }
    }

    /**
     * Verifica se ainda há vagas no quarto.
     */
    public boolean temVaga() {
        return pacientes != null && pacientes.size() < capacidade;
    }
}
