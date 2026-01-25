package io.github.guerramath.safety_api.dto;

public class SafetyAssessmentDTO {

    private String pilotName;
    // Dados de entrada básicos
    private int healthScore;
    private int weatherScore;
    private int aircraftScore;
    private int missionScore;

    // Campo novo para atender ao DOC 9859 (Mitigação)
    private String mitigationPlan;

    // Getters e Setters
    public String getPilotName() {
        return pilotName;
    }

    public void setPilotName(String pilotName) {
        this.pilotName = pilotName;
    }

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
}