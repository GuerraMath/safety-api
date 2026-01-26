package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.repository.SafetyRepository;
import io.github.guerramath.safety_api.exception.SafetyValidationException;
import io.github.guerramath.safety_api.service.SafetyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SafetyServiceTest {

    @Mock
    private SafetyRepository safetyRepository;

    @InjectMocks
    private SafetyService safetyService;

    @Test
    @DisplayName("Deve lançar exceção quando o risco for ALTO e não houver plano de mitigação")
    void deveBloquearRiscoAltoSemMitigacao() {
        // Cenário: Todos os scores no máximo (Risco Alto) e plano vazio
        SafetyEvaluation evaluation = new SafetyEvaluation();
        evaluation.setPilotName("Matheus Guerra");
        evaluation.setHealthScore(5);
        evaluation.setWeatherScore(5);
        evaluation.setAircraftScore(5);
        evaluation.setMissionScore(5);
        evaluation.setMitigationPlan("");

        // Ação & Verificação
        assertThrows(SafetyValidationException.class, () -> {
            safetyService.saveEvaluation(evaluation);
        });

        // Garante que o repositório NUNCA foi chamado para salvar
        verify(safetyRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve salvar com sucesso quando o risco for ALTO mas houver plano de mitigação")
    void deveSalvarRiscoAltoComMitigacao() {
        // Cenário: Risco Alto com plano preenchido
        SafetyEvaluation evaluation = new SafetyEvaluation();
        evaluation.setHealthScore(5);
        evaluation.setMitigationPlan("Utilização de segundo piloto e reserva de combustível extra.");

        // Simulação do comportamento do repositório
        when(safetyRepository.save(any())).thenReturn(evaluation);

        // Ação
        SafetyEvaluation saved = safetyService.saveEvaluation(evaluation);

        // Verificação
        assertNotNull(saved);
        verify(safetyRepository, times(1)).save(evaluation);
    }
}