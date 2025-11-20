package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@DisplayName("Testes para LeituraSensorRepository")
class LeituraSensorRepositoryTest {

    @Autowired
    private LeituraSensorRepository leituraSensorRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Atendimento atendimento1;
    private Atendimento atendimento2;
    private LeituraSensor leituraSensor1;
    private LeituraSensor leituraSensor2;
    private LeituraSensor leituraSensor3;

    @BeforeEach
    void setUp() {
        leituraSensorRepository.deleteAll();
        entityManager.clear();

        atendimento1 = new Atendimento();
        atendimento1.setDataEntrada(LocalDateTime.now());
        atendimento1.setNumeroQuarto(101);
        atendimento1.setDiagnostico(Diagnostico.A02);
        atendimento1.setTratamento("Antibiótico");
        entityManager.persist(atendimento1);

        atendimento2 = new Atendimento();
        atendimento2.setDataEntrada(LocalDateTime.now().minusDays(1));
        atendimento2.setNumeroQuarto(102);
        atendimento2.setDiagnostico(Diagnostico.J15);
        atendimento2.setTratamento("Analgésico");
        entityManager.persist(atendimento2);
        entityManager.flush();

        leituraSensor1 = LeituraSensor.builder()
                .valor(36.5)
                .dataHora(LocalDateTime.now().minusHours(2))
                .tipoDado(TipoDado.TEMPERATURA)
                .unidadeMedida(UnidadeMedida.CELSIUS)
                .atendimento(atendimento1)
                .gravidade(Gravidade.NORMAL)
                .condicaoSaude(CondicaoSaude.NORMAL)
                .build();
        entityManager.persist(leituraSensor1);

        leituraSensor2 = LeituraSensor.builder()
                .valor(120.0)
                .dataHora(LocalDateTime.now().minusHours(1))
                .tipoDado(TipoDado.PRESSAO_ARTERIAL)
                .unidadeMedida(UnidadeMedida.MMHG)
                .atendimento(atendimento1)
                .gravidade(Gravidade.ALERTA)
                .condicaoSaude(CondicaoSaude.HIPERTENSAO_ESTAGIO_1)
                .build();
        entityManager.persist(leituraSensor2);

        leituraSensor3 = LeituraSensor.builder()
                .valor(98.0)
                .dataHora(LocalDateTime.now().minusMinutes(30))
                .tipoDado(TipoDado.PRESSAO_ARTERIAL)
                .unidadeMedida(UnidadeMedida.MMHG)
                .atendimento(atendimento2)
                .gravidade(Gravidade.EMERGENCIAL)
                .condicaoSaude(CondicaoSaude.TAQUICARDIA)
                .build();
        entityManager.persist(leituraSensor3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Deve encontrar leituras por ID de atendimento")
    void findByAtendimentoId_shouldReturnLeiturasForGivenAtendimentoId() {
        List<LeituraSensor> leiturasAtendimento1 = leituraSensorRepository.findByAtendimentoId(atendimento1.getId());
        assertNotNull(leiturasAtendimento1);
        assertEquals(2, leiturasAtendimento1.size());
        assertTrue(leiturasAtendimento1.contains(leituraSensor1));
        assertTrue(leiturasAtendimento1.contains(leituraSensor2));

        List<LeituraSensor> leiturasAtendimento2 = leituraSensorRepository.findByAtendimentoId(atendimento2.getId());
        assertNotNull(leiturasAtendimento2);
        assertEquals(1, leiturasAtendimento2.size());
        assertTrue(leiturasAtendimento2.contains(leituraSensor3));

        List<LeituraSensor> leiturasAtendimentoInexistente = leituraSensorRepository.findByAtendimentoId(999L);
        assertNotNull(leiturasAtendimentoInexistente);
        assertTrue(leiturasAtendimentoInexistente.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar todas as leituras")
    void findAll_shouldReturnAllLeituras() {
        List<LeituraSensor> allLeituras = leituraSensorRepository.findAll();
        assertNotNull(allLeituras);
        assertEquals(3, allLeituras.size());
        assertTrue(allLeituras.contains(leituraSensor1));
        assertTrue(allLeituras.contains(leituraSensor2));
        assertTrue(allLeituras.contains(leituraSensor3));
    }
}
