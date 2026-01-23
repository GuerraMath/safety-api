package io.github.guerramath.safety_api.model; // Define a localização exata do arquivo

import jakarta.persistence.*; // Importa as anotações de banco de dados (Entity, Table, Id, etc)
import java.time.LocalDateTime; // Necessário para o campo de data e hora

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

    private String riskLevel; // LOW, MEDIUM, HIGH
    private String comments;

    // Construtores, Getters e Setters virão aqui em seguida

    // Construtor Vazio (Obrigatório para o JPA)
    public SafetyAssessment() {}

    // Getters e Setters (Essenciais para o tráfego de dados)
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

    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
  }