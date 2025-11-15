package com.springwalker.back.monitoramento.enums.leitura;

public enum CondicaoSaude {
    // Para Temperatura
    FEBRE_ALTA,
    FEBRE,
    ESTADO_FEBRIL,
    NORMAL,
    HIPOTERMIA,
    HIPORTEMIA_SEVERA,

    // Para Frequência Cardíaca
    TAQUICARDIA,
    BRADICARDIA,

    // Para Pressão Arterial
    CRISE_HIPERTENSIVA,
    HIPERTENSAO_ESTAGIO_2,
    HIPERTENSAO_ESTAGIO_1,
    PRE_HIPERTENSAO,
    HIPOTENSAO,

    // Para Medicação
    MEDICACAO_FINALIZANDO
}
