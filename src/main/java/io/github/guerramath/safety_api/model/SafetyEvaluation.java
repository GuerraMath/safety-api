package io.github.guerramath.safety_api.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "safety_evaluations")
public class SafetyEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pilot_name", nullable = false)
    private String pilotName;

    private int healthScore;
    private int weatherScore;
    private int aircraftScore;
    private int missionScore;

    @Column(columnDefinition = "TEXT")
    private String mitigationPlan;

    @Enumerated(EnumType.STRING)
    private RiskLevel riskLevel;

    private LocalDateTime dateTime;

    public SafetyEvaluation() {}

    // Getters e Setters Essenciais
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPilotName() { return pilotName; }
    public void setPilotName(String pilotName) { this.pilotName = pilotName; }
    public int getHealthScore() { return healthScore; }
    public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
    public int getWeatherScore() { return weatherScore; }
    public void setWeatherScore(int weatherScore) { this.weatherScore = weatherScore; }
    public int getAircraftScore() { return aircraftScore; }
    public void setAircraftScore(int aircraftScore) { this.aircraftScore = aircraftScore; }
    public int getMissionScore() { return missionScore; }
    public void setMissionScore(int missionScore) { this.missionScore = missionScore; }
    public String getMitigationPlan() { return mitigationPlan; }
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
}