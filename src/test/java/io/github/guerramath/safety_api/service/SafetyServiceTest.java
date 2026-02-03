package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.repository.SafetyRepository;
import io.github.guerramath.safety_api.exception.SafetyValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
// IMPORTS DO SPRING REMOVIDOS AQUI (Não precisamos deles)

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Apenas Mockito, sem peso do Spring
public class SafetyServiceTest {

    @Mock
    private SafetyRepository safetyRepository;

    @InjectMocks
    private SafetyService safetyService;

    @Test
    @DisplayName("Deve lancar excecao quando o risco for ALTO e nao houver plano de mitigacao")
    void deveBloquearRiscoAltoSemMitigacao() {
        // Cenario
        SafetyEvaluation evaluation = new SafetyEvaluation();
        evaluation.setPilotName("Matheus Guerra");
        evaluation.setHealthScore(15);
        evaluation.setWeatherScore(15);
        evaluation.setAircraftScore(15);
        evaluation.setMissionScore(15);
        evaluation.setMitigationPlan("");

        // Acao & Verificacao
        assertThrows(SafetyValidationException.class, () -> {
            safetyService.saveEvaluation(evaluation);
        });

        verify(safetyRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar com sucesso quando o risco for ALTO mas houver plano de mitigacao")
    void deveSalvarRiscoAltoComMitigacao() {
        // Cenario
        SafetyEvaluation evaluation = new SafetyEvaluation();
        evaluation.setPilotName("Matheus Guerra");
        evaluation.setHealthScore(15);
        evaluation.setWeatherScore(15);
        evaluation.setAircraftScore(15);
        evaluation.setMissionScore(15);
        evaluation.setMitigationPlan("Utilizacao de segundo piloto e reserva de combustivel.");

        when(safetyRepository.save(any(SafetyEvaluation.class))).thenReturn(evaluation);

        // Acao
        SafetyEvaluation saved = safetyService.saveEvaluation(evaluation);

        // Verificacao
        assertNotNull(saved);
        verify(safetyRepository, times(1)).save(any(SafetyEvaluation.class));
    }
}