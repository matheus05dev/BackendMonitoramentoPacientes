package com.springwalker.back.monitoramento.service.leitura.processamento;

import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.service.leitura.processamento.strategies.AnaliseDadosSensorStrategy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class AnaliseDadosSensorService {

    private final Map<TipoDado, AnaliseDadosSensorStrategy> strategies;

    public AnaliseDadosSensorService(List<AnaliseDadosSensorStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(AnaliseDadosSensorStrategy::getSupportedTipoDado, Function.identity()));
    }

    public void analisarDadosSensor(LeituraSensor leitura) {
        TipoDado tipoDado = leitura.getTipoDado();
        AnaliseDadosSensorStrategy strategy = strategies.get(tipoDado);

        if (strategy == null) {
            throw new IllegalArgumentException("Nenhuma estratégia de análise encontrada para o tipo de dado: " + tipoDado);
        }
        strategy.analisar(leitura);
    }
}
