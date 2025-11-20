package com.springwalker.back.monitoramento.repository;

import com.springwalker.back.atendimento.enums.Diagnostico;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.monitoramento.enums.leitura.CondicaoSaude;
import com.springwalker.back.monitoramento.enums.leitura.Gravidade;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.enums.leitura.UnidadeMedida;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DisplayName("Testes para NotificacaoRepository")
class NotificacaoRepositoryTest {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Atendimento atendimento;
    private LeituraSensor leituraSensor1;
    private LeituraSensor leituraSensor2;
    private LeituraSensor leituraSensor3; // New LeituraSensor for notificacao3
    private Notificacao notificacao1;
    private Notificacao notificacao2;
    private Notificacao notificacao3;

    @BeforeEach
    void setUp() {
        notificacaoRepository.deleteAll();
        entityManager.clear();

        atendimento = new Atendimento();
        atendimento.setId(null);
        atendimento.setDataEntrada(LocalDateTime.now());
        atendimento.setNumeroQuarto(101);
        atendimento.setDiagnostico(Diagnostico.A02); // Example value
        atendimento.setTratamento("Example Treatment"); // Example value
        entityManager.persist(atendimento);
        entityManager.flush();

        leituraSensor1 = LeituraSensor.builder()
                .valor(38.5)
                .dataHora(LocalDateTime.now().minusHours(2))
                .tipoDado(TipoDado.TEMPERATURA)
                .unidadeMedida(UnidadeMedida.CELSIUS)
                .atendimento(atendimento)
                .gravidade(Gravidade.ALERTA)
                .condicaoSaude(CondicaoSaude.FEBRE_ALTA)
                .build();
        entityManager.persist(leituraSensor1);

        leituraSensor2 = LeituraSensor.builder()
                .valor(90.0)
                .dataHora(LocalDateTime.now().minusHours(1))
                .tipoDado(TipoDado.PRESSAO_ARTERIAL)
                .unidadeMedida(UnidadeMedida.MMHG)
                .atendimento(atendimento)
                .gravidade(Gravidade.NORMAL)
                .condicaoSaude(CondicaoSaude.HIPERTENSAO_ESTAGIO_2)
                .build();
        entityManager.persist(leituraSensor2);

        // Create a new LeituraSensor for notificacao3
        leituraSensor3 = LeituraSensor.builder()
                .valor(37.0)
                .dataHora(LocalDateTime.now().minusMinutes(15))
                .tipoDado(TipoDado.TEMPERATURA)
                .unidadeMedida(UnidadeMedida.CELSIUS)
                .atendimento(atendimento)
                .gravidade(Gravidade.NORMAL)
                .condicaoSaude(CondicaoSaude.NORMAL)
                .build();
        entityManager.persist(leituraSensor3);
        entityManager.flush();

        notificacao1 = Notificacao.builder()
                .leituraSensor(leituraSensor1)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now().minusHours(2))
                .numeroQuarto(atendimento.getNumeroQuarto())
                .build();
        entityManager.persist(notificacao1);

        notificacao2 = Notificacao.builder()
                .leituraSensor(leituraSensor2)
                .status(StatusNotificacao.FECHADA)
                .dataCriacao(LocalDateTime.now().minusHours(1))
                .dataFechamento(LocalDateTime.now().minusMinutes(30))
                .numeroQuarto(atendimento.getNumeroQuarto())
                .build();
        entityManager.persist(notificacao2);

        notificacao3 = Notificacao.builder()
                .leituraSensor(leituraSensor3) // Use the new LeituraSensor3
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now().minusMinutes(10))
                .numeroQuarto(atendimento.getNumeroQuarto())
                .build();
        entityManager.persist(notificacao3);
        entityManager.flush();
    }

    @Test
    @DisplayName("Deve encontrar notificações por status")
    void findByStatus_shouldReturnNotificacoesWithGivenStatus() {
        List<Notificacao> abertas = notificacaoRepository.findByStatus(StatusNotificacao.ABERTA);
        assertNotNull(abertas);
        assertEquals(2, abertas.size());
        assertTrue(abertas.stream().allMatch(n -> n.getStatus() == StatusNotificacao.ABERTA));

        List<Notificacao> fechadas = notificacaoRepository.findByStatus(StatusNotificacao.FECHADA);
        assertNotNull(fechadas);
        assertEquals(1, fechadas.size());
        assertTrue(fechadas.stream().allMatch(n -> n.getStatus() == StatusNotificacao.FECHADA));

        List<Notificacao> emAtendimento = notificacaoRepository.findByStatus(StatusNotificacao.EM_ATENDIMENTO);
        assertNotNull(emAtendimento);
        assertTrue(emAtendimento.isEmpty());
    }

    @Test
    @DisplayName("Deve encontrar todas as notificações ordenadas por data de criação descendente")
    void findAllByOrderByDataCriacaoDesc_shouldReturnAllNotificacoesOrderedByCreationDateDesc() {
        List<Notificacao> notificacoes = notificacaoRepository.findAllByOrderByDataCriacaoDesc();
        assertNotNull(notificacoes);
        assertEquals(3, notificacoes.size());
        assertEquals(notificacao3.getId(), notificacoes.get(0).getId()); // Most recent
        assertEquals(notificacao2.getId(), notificacoes.get(1).getId());
        assertEquals(notificacao1.getId(), notificacoes.get(2).getId()); // Oldest
    }

    @Test
    @DisplayName("Deve encontrar todas as notificações com detalhes de leitura e atendimento")
    void findAllWithDetails_shouldReturnAllNotificacoesWithLeituraSensorAndAtendimentoDetails() {
        List<Notificacao> notificacoes = notificacaoRepository.findAllWithDetails();
        assertNotNull(notificacoes);
        assertEquals(3, notificacoes.size());

        // Verify that associated entities are eagerly fetched
        notificacoes.forEach(n -> {
            assertNotNull(n.getLeituraSensor());
            assertNotNull(n.getLeituraSensor().getAtendimento());
            assertEquals(atendimento.getId(), n.getLeituraSensor().getAtendimento().getId());
        });
    }

    @Test
    @DisplayName("Deve encontrar a primeira notificação por atendimento, tipo de dado e status, ordenada por data de criação descendente")
    void findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc_shouldReturnCorrectNotificacao() {
        // Test for an existing notification
        Optional<Notificacao> foundNotificacao = notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimento.getId(), TipoDado.TEMPERATURA, StatusNotificacao.ABERTA);

        assertTrue(foundNotificacao.isPresent());
        assertEquals(notificacao3.getId(), foundNotificacao.get().getId()); // Should be the most recent ABERTA for TEMPERATURA

        // Test for a non-existing notification
        Optional<Notificacao> notFoundNotificacao = notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimento.getId(), TipoDado.TEMPERATURA, StatusNotificacao.EM_ATENDIMENTO);
        assertFalse(notFoundNotificacao.isPresent());

        // Test for a different type of data
        Optional<Notificacao> foundNotificacaoPressao = notificacaoRepository.findFirstByLeituraSensorAtendimentoIdAndLeituraSensorTipoDadoAndStatusOrderByDataCriacaoDesc(
                atendimento.getId(), TipoDado.PRESSAO_ARTERIAL, StatusNotificacao.FECHADA);
        assertTrue(foundNotificacaoPressao.isPresent());
        assertEquals(notificacao2.getId(), foundNotificacaoPressao.get().getId());
    }
}
