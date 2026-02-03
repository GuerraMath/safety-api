package io.github.guerramath.safety_api.model;

import io.github.guerramath.safety_api.dto.SafetyAssessmentDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelCoverageTest {

    @Test
    @DisplayName("Deve garantir que a data de criação seja gerada automaticamente")
    void testPrePersistDate() {
        SafetyEvaluation eval = new SafetyEvaluation();
        // Simulando o comportamento do JPA/Hibernate
        eval.onCreate();
        assertNotNull(eval.getDateTime());
    }

    @Test
    @DisplayName("Deve cobrir Getters e Setters dos DTOs")
    void testDtoAccessors() {
        SafetyAssessmentDTO dto = new SafetyAssessmentDTO();
        dto.setPilotName("Teste");
        assertEquals("Teste", dto.getPilotName());
    }
}