package com.springwalker.back.monitoramento.service.notificacao.processamento;

import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.monitoramento.enums.notificacao.StatusNotificacao;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.model.Notificacao;
import com.springwalker.back.monitoramento.repository.NotificacaoRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para CriadorNotificacaoService")
class CriadorNotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;
    @Mock
    private AtendimentoRepository atendimentoRepository;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private CriadorNotificacaoService criadorNotificacaoService;

    @Test
    @DisplayName("Deve criar e gravar notificação com numeroQuarto do atendimento")
    void criarEGravarNotificacao_shouldCreateAndSaveWithNumeroQuarto() {
        Long atendimentoId = 1L;
        Integer numeroQuarto = 101;

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);
        atendimento.setNumeroQuarto(numeroQuarto);

        LeituraSensor leitura = new LeituraSensor();
        leitura.setAtendimento(atendimento);

        Notificacao expectedNotificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now()) // Will be set by service
                .numeroQuarto(numeroQuarto)
                .build();

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(expectedNotificacao);

        Notificacao result = criadorNotificacaoService.criarEGravarNotificacao(leitura);

        assertNotNull(result);
        assertEquals(numeroQuarto, result.getNumeroQuarto());
        assertEquals(StatusNotificacao.ABERTA, result.getStatus());
        assertNotNull(result.getDataCriacao());
        assertEquals(leitura, result.getLeituraSensor());

        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    @DisplayName("Deve criar e gravar notificação sem numeroQuarto se atendimento for nulo")
    void criarEGravarNotificacao_shouldCreateAndSaveWithoutNumeroQuarto_whenAtendimentoIsNull() {
        LeituraSensor leitura = new LeituraSensor(); // LeituraSensor sem atendimento

        Notificacao expectedNotificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .numeroQuarto(null)
                .build();

        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(expectedNotificacao);

        Notificacao result = criadorNotificacaoService.criarEGravarNotificacao(leitura);

        assertNotNull(result);
        assertNull(result.getNumeroQuarto());
        assertEquals(StatusNotificacao.ABERTA, result.getStatus());
        assertNotNull(result.getDataCriacao());
        assertEquals(leitura, result.getLeituraSensor());

        verify(entityManager, never()).clear();
        verify(atendimentoRepository, never()).findById(any());
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    @DisplayName("Deve criar e gravar notificação sem numeroQuarto se atendimento não tiver numeroQuarto")
    void criarEGravarNotificacao_shouldCreateAndSaveWithoutNumeroQuarto_whenAtendimentoHasNoNumeroQuarto() {
        Long atendimentoId = 1L;

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);
        atendimento.setNumeroQuarto(null); // NumeroQuarto é nulo

        LeituraSensor leitura = new LeituraSensor();
        leitura.setAtendimento(atendimento);

        Notificacao expectedNotificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .numeroQuarto(null)
                .build();

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(expectedNotificacao);

        Notificacao result = criadorNotificacaoService.criarEGravarNotificacao(leitura);

        assertNotNull(result);
        assertNull(result.getNumeroQuarto());
        assertEquals(StatusNotificacao.ABERTA, result.getStatus());
        assertNotNull(result.getDataCriacao());
        assertEquals(leitura, result.getLeituraSensor());

        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }

    @Test
    @DisplayName("Deve criar e gravar notificação sem numeroQuarto se atendimento não for encontrado")
    void criarEGravarNotificacao_shouldCreateAndSaveWithoutNumeroQuarto_whenAtendimentoNotFound() {
        Long atendimentoId = 1L;

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);

        LeituraSensor leitura = new LeituraSensor();
        leitura.setAtendimento(atendimento);

        Notificacao expectedNotificacao = Notificacao.builder()
                .leituraSensor(leitura)
                .status(StatusNotificacao.ABERTA)
                .dataCriacao(LocalDateTime.now())
                .numeroQuarto(null)
                .build();

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.empty());
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(expectedNotificacao);

        Notificacao result = criadorNotificacaoService.criarEGravarNotificacao(leitura);

        assertNotNull(result);
        assertNull(result.getNumeroQuarto());
        assertEquals(StatusNotificacao.ABERTA, result.getStatus());
        assertNotNull(result.getDataCriacao());
        assertEquals(leitura, result.getLeituraSensor());

        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(notificacaoRepository, times(1)).save(any(Notificacao.class));
    }
}
