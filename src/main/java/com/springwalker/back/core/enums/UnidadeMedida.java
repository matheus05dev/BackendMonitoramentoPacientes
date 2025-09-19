package com.springwalker.back.core.enums;

public enum UnidadeMedida {
    MMHG("mmHg"),
    CELSIUS("°C"),
    BPM("bpm");

    private final String simbolo;

    UnidadeMedida(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getSimbolo() {
        return simbolo;
    }
}
