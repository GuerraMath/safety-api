package io.github.guerramath.safety_api.service;

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

    public SafetyAssessment salvarAvaliacao(SafetyAssessment assessment) {
        // Lógica de cálculo de risco (Fatores Humanos)
        int soma = assessment.getHealthScore() + assessment.getWeatherScore() +
                assessment.getAircraftScore() + assessment.getMissionScore();

        if (soma <= 8) assessment.setRiskLevel("BAIXO");
        else if (soma <= 15) assessment.setRiskLevel("MÉDIO");
        else assessment.setRiskLevel("ALTO");

        assessment.setDateTime(LocalDateTime.now());
        return repository.save(assessment);
    }

    public List<SafetyAssessment> buscarTodos() {
        return repository.findAll();
    }
}