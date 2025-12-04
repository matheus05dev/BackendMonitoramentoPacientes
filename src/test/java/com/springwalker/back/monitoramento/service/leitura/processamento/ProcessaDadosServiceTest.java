package com.springwalker.back.monitoramento.service.leitura.processamento;

import com.springwalker.back.atendimento.enums.StatusMonitoramento;
import com.springwalker.back.atendimento.model.Atendimento;
import com.springwalker.back.atendimento.repository.AtendimentoRepository;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorRequestDTO;
import com.springwalker.back.monitoramento.dto.leitura.LeituraSensorResponseDTO;
import com.springwalker.back.monitoramento.enums.leitura.TipoDado;
import com.springwalker.back.monitoramento.mapper.LeituraSensorMapper;
import com.springwalker.back.monitoramento.model.LeituraSensor;
import com.springwalker.back.monitoramento.repository.LeituraSensorRepository;
import com.springwalker.back.monitoramento.service.notificacao.processamento.GerenciadorNotificacaoService;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para ProcessaDadosService")
class ProcessaDadosServiceTest {

    @Mock
    private LeituraSensorRepository leituraSensorRepository;
    @Mock
    private AtendimentoRepository atendimentoRepository;
    @Mock
    private LeituraSensorMapper leituraSensorMapper;
    private GerenciadorNotificacaoService gerenciadorNotificacaoService;
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private ProcessaDadosService processaDadosService;

    @Test
    @DisplayName("Deve salvar leitura com sucesso e processar notificação se não for medicação")
    void salvar_shouldSaveLeituraAndProcessNotification_whenNotMedicacao() {
        Long atendimentoId = 1L;
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();
        requestDTO.setTipoDado(TipoDado.TEMPERATURA);

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);
        atendimento.setStatusMonitoramento(StatusMonitoramento.MONITORANDO);

        LeituraSensor leituraSensor = new LeituraSensor();
        leituraSensor.setTipoDado(TipoDado.TEMPERATURA);

        LeituraSensor savedLeitura = new LeituraSensor();
        savedLeitura.setId(10L);
        savedLeitura.setTipoDado(TipoDado.TEMPERATURA);

        LeituraSensorResponseDTO responseDTO = new LeituraSensorResponseDTO();
        responseDTO.setId(10L);

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));
        when(leituraSensorMapper.toModel(requestDTO)).thenReturn(leituraSensor);
        when(leituraSensorRepository.save(any(LeituraSensor.class))).thenReturn(savedLeitura);
        when(leituraSensorMapper.toResponse(savedLeitura)).thenReturn(responseDTO);

        LeituraSensorResponseDTO result = processaDadosService.salvar(atendimentoId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(leituraSensorMapper, times(1)).toModel(requestDTO);
        verify(leituraSensorRepository, times(1)).save(leituraSensor);
        verify(gerenciadorNotificacaoService, times(1)).processarEEnviarNotificacao(savedLeitura);
        verify(leituraSensorMapper, times(1)).toResponse(savedLeitura);
    }

    @Test
    @DisplayName("Deve salvar leitura com sucesso e NÃO processar notificação se for medicação")
    void salvar_shouldSaveLeituraAndNotProcessNotification_whenMedicacao() {
        Long atendimentoId = 1L;
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();
        requestDTO.setTipoDado(TipoDado.MEDICACAO);

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);
        atendimento.setStatusMonitoramento(StatusMonitoramento.MONITORANDO);

        LeituraSensor leituraSensor = new LeituraSensor();
        leituraSensor.setTipoDado(TipoDado.MEDICACAO);

        LeituraSensor savedLeitura = new LeituraSensor();
        savedLeitura.setId(10L);
        savedLeitura.setTipoDado(TipoDado.MEDICACAO);

        LeituraSensorResponseDTO responseDTO = new LeituraSensorResponseDTO();
        responseDTO.setId(10L);

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));
        when(leituraSensorMapper.toModel(requestDTO)).thenReturn(leituraSensor);
        when(leituraSensorRepository.save(any(LeituraSensor.class))).thenReturn(savedLeitura);
        when(leituraSensorMapper.toResponse(savedLeitura)).thenReturn(responseDTO);

        LeituraSensorResponseDTO result = processaDadosService.salvar(atendimentoId, requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getId(), result.getId());
        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(leituraSensorMapper, times(1)).toModel(requestDTO);
        verify(leituraSensorRepository, times(1)).save(leituraSensor);
        verify(gerenciadorNotificacaoService, never()).processarEEnviarNotificacao(any(LeituraSensor.class));
        verify(leituraSensorMapper, times(1)).toResponse(savedLeitura);
    }

    @Test
    @DisplayName("Deve lançar RuntimeException se atendimento não for encontrado")
    void salvar_shouldThrowException_whenAtendimentoNotFound() {
        Long atendimentoId = 1L;
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                processaDadosService.salvar(atendimentoId, requestDTO));

        assertEquals("Atendimento não encontrado com o ID: " + atendimentoId, exception.getMessage());
        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(leituraSensorMapper, never()).toModel(any());
        verify(leituraSensorRepository, never()).save(any());
        verify(gerenciadorNotificacaoService, never()).processarEEnviarNotificacao(any());
        verify(leituraSensorMapper, never()).toResponse(any());
    }

    @Test
    @DisplayName("Deve lançar RuntimeException se atendimento não estiver em monitoramento")
    void salvar_shouldThrowException_whenAtendimentoNotMonitoring() {
        Long atendimentoId = 1L;
        LeituraSensorRequestDTO requestDTO = new LeituraSensorRequestDTO();

        Atendimento atendimento = new Atendimento();
        atendimento.setId(atendimentoId);
        atendimento.setStatusMonitoramento(StatusMonitoramento.NAO_MONITORADO);

        when(atendimentoRepository.findById(atendimentoId)).thenReturn(Optional.of(atendimento));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                processaDadosService.salvar(atendimentoId, requestDTO));

        assertEquals("O atendimento com ID " + atendimentoId + " não está em modo de monitoramento.", exception.getMessage());
        verify(entityManager, times(1)).clear();
        verify(atendimentoRepository, times(1)).findById(atendimentoId);
        verify(leituraSensorMapper, never()).toModel(any());
        verify(leituraSensorRepository, never()).save(any());
        verify(gerenciadorNotificacaoService, never()).processarEEnviarNotificacao(any());
        verify(leituraSensorMapper, never()).toResponse(any());
    }
}
