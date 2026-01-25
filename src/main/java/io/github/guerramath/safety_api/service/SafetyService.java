package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.dto.SafetyAssessmentDTO;
import io.github.guerramath.safety_api.model.RiskLevel; // Import novo
import io.github.guerramath.safety_api.model.SafetyAssessment;
import io.github.guerramath.safety_api.repository.SafetyAssessmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SafetyService {

    @Autowired
    private SafetyAssessmentRepository repository;

    private static final int THRESHOLD_LOW = 20;   // Ajuste conforme sua matriz real
    private static final int THRESHOLD_MEDIUM = 40;

    public SafetyAssessment salvarAvaliacao(SafetyAssessmentDTO dto) {
        SafetyAssessment assessment = new SafetyAssessment();
        assessment.setPilotName(dto.getPilotName());
        assessment.setHealthScore(dto.getHealthScore());
        assessment.setWeatherScore(dto.getWeatherScore());
        assessment.setAircraftScore(dto.getAircraftScore());
        assessment.setMissionScore(dto.getMissionScore());

        // --- LÓGICA DE INTELIGÊNCIA DE SEGURANÇA ---

        RiskLevel nivelCalculado;

        // 1. Verificação de KILL ITEMS (Regras Impeditivas)
        // Exemplo: Se AircraftScore for muito alto (ex: pneu careca + sem freio), é NO_GO direto.
        if (dto.getAircraftScore() >= 50) {
            nivelCalculado = RiskLevel.NO_GO;
        }
        else {
            // 2. Cálculo Padrão (Soma)
            int soma = assessment.getHealthScore() + assessment.getWeatherScore() +
                    assessment.getAircraftScore() + assessment.getMissionScore();

            if (soma <= THRESHOLD_LOW) nivelCalculado = RiskLevel.LOW;
            else if (soma <= THRESHOLD_MEDIUM) nivelCalculado = RiskLevel.MEDIUM;
            else nivelCalculado = RiskLevel.HIGH;
        }

        // 3. Validação de Mitigação (Compliance)
        if (nivelCalculado == RiskLevel.HIGH || nivelCalculado == RiskLevel.NO_GO) {
            // Se o risco é alto e o piloto não escreveu nada no plano de mitigação...
            if (dto.getMitigationPlan() == null || dto.getMitigationPlan().trim().isEmpty()) {
                // Por enquanto lançamos uma RuntimeException simples para travar o processo
                throw new RuntimeException("ALERTA DE SEGURANÇA: Operações de alto risco exigem um Plano de Mitigação preenchido.");
            }
        }

        assessment.setRiskLevel(nivelCalculado);
        assessment.setDateTime(LocalDateTime.now());

        return repository.save(assessment);
    }

    public List<SafetyAssessment> buscarTodos() {
        return repository.findAll();
    }
    public List<SafetyAssessment> getHistory() {
        return repository.findAll(); // Retorna todos os registros para a auditoria
    }
}