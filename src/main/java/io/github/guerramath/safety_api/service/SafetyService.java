package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.dto.SafetyAssessmentDTO;
import io.github.guerramath.safety_api.model.RiskLevel;
import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.repository.SafetyRepository;
import io.github.guerramath.safety_api.exception.SafetyValidationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SafetyService {

    private final SafetyRepository repository;

    private static final int THRESHOLD_LOW = 20;
    private static final int THRESHOLD_MEDIUM = 40;

    public SafetyService(SafetyRepository repository) {
        this.repository = repository;
    }

    /**
     * Versão principal para salvar via API (Dashboard)
     */
    public SafetyEvaluation saveEvaluation(SafetyAssessmentDTO dto) {
        SafetyEvaluation evaluation = new SafetyEvaluation();
        evaluation.setPilotName(dto.getPilotName());
        evaluation.setHealthScore(dto.getHealthScore());
        evaluation.setWeatherScore(dto.getWeatherScore());
        evaluation.setAircraftScore(dto.getAircraftScore());
        evaluation.setMissionScore(dto.getMissionScore());
        evaluation.setMitigationPlan(dto.getMitigationPlan());

        return processRiskAndSave(evaluation);
    }

    /**
     * Versão para os testes unitários (recebe a entidade diretamente)
     */
    public SafetyEvaluation saveEvaluation(SafetyEvaluation evaluation) {
        return processRiskAndSave(evaluation);
    }

    private SafetyEvaluation processRiskAndSave(SafetyEvaluation evaluation) {
        RiskLevel nivelCalculado;

        // 1. Verificação de KILL ITEMS (Aeronave >= 50 = NO_GO)
        if (evaluation.getAircraftScore() >= 50) {
            nivelCalculado = RiskLevel.NO_GO;
        } else {
            // 2. Cálculo Padrão (Soma dos fatores)
            int soma = evaluation.getHealthScore() + evaluation.getWeatherScore() +
                    evaluation.getAircraftScore() + evaluation.getMissionScore();

            if (soma <= THRESHOLD_LOW) nivelCalculado = RiskLevel.LOW;
            else if (soma <= THRESHOLD_MEDIUM) nivelCalculado = RiskLevel.MEDIUM;
            else nivelCalculado = RiskLevel.HIGH;
        }

        // 3. Validação de Compliance SMS
        if (nivelCalculado == RiskLevel.HIGH || nivelCalculado == RiskLevel.NO_GO) {
            if (evaluation.getMitigationPlan() == null || evaluation.getMitigationPlan().trim().isEmpty()) {
                throw new SafetyValidationException("ALERTA DE SEGURANÇA: Operações de alto risco exigem Plano de Mitigação.");
            }
        }

        evaluation.setRiskLevel(nivelCalculado);
        evaluation.setDateTime(LocalDateTime.now());

        return repository.save(evaluation);
    }

    public List<SafetyEvaluation> getHistory() {
        return repository.findAll();
    }
}