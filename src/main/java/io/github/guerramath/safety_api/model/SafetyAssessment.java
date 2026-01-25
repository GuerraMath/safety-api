package io.github.guerramath.safety_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessments")
public class SafetyAssessment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pilotName;
    private LocalDateTime dateTime;

    // Scores de 1 a 5 para cada pilar (Fatores Humanos)
    private Integer healthScore;
    private Integer weatherScore;
    private Integer aircraftScore;
    private Integer missionScore;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel; // Agora é do tipo Enum, não String

    // Construtor Vazio
    public SafetyAssessment() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPilotName() { return pilotName; }
    public void setPilotName(String pilotName) { this.pilotName = pilotName; }

    public Integer getHealthScore() { return healthScore; }
    public void setHealthScore(Integer healthScore) { this.healthScore = healthScore; }

    public Integer getWeatherScore() { return weatherScore; }
    public void setWeatherScore(Integer weatherScore) { this.weatherScore = weatherScore; }

    public Integer getAircraftScore() { return aircraftScore; }
    public void setAircraftScore(Integer aircraftScore) { this.aircraftScore = aircraftScore; }

    public Integer getMissionScore() { return missionScore; }
    public void setMissionScore(Integer missionScore) { this.missionScore = missionScore; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    // --- AQUI ESTAVA O ERRO: Removemos a versão String ---

    // Mantemos APENAS esta versão correta com o Enum:
    public RiskLevel getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }
}