package io.github.guerramath.safety_api.model;

public enum RiskLevel {
    LOW,        // Operação Normal
    MEDIUM,     // Requer Atenção
    HIGH,       // Requer Mitigação
    NO_GO       // Voo Proibido (Kill Item acionado)
}