package com.springwalker.back.monitoramento.enums.leitura;

import lombok.Getter;

@Getter
public enum UnidadeMedida {
    MMHG("mmHg"),
    CELSIUS("°C"),
    BPM("bpm"),
    GRAMAS("g");

    private final String simbolo;

    UnidadeMedida(String simbolo) {
        this.simbolo = simbolo;
    }

}
