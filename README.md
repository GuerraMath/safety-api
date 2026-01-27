Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![CI](https://github.com/GuerraMath/safety-api/actions/workflows/ci.yml/badge.svg)
![Coverage](https://codecov.io/gh/GuerraMath/safety-api/branch/main/graph/badge.svg)
[![Railway](https://img.shields.io/badge/Deploy-Railway-blueviolet?logo=railway)](https://safety-api-production.up.railway.app/)
[![Português](https://img.shields.io/badge/Language-Português-green)](READMEpt-br.md)

🌐 **Live Demo**: [https://safety-api-production.up.railway.app](https://safety-api-production.up.railway.app)

Full-Stack Operational Risk Management System based on Endsley's Situational Awareness framework.

## System Interface

![Preview do Dashboard](./assets/dashboard-preview.png)
- Risk assessment and team readiness dashboard. Responsive checklist-style interface with traceability and print functionality, integrated via API.

##🎯 Quick Start

**Test it NOW without installing anything:**

1. Access: [Live Demo](https://safety-api-production.up.railway.app/)

2. Test the API: [Swagger Docs](https://safety-api-production.up.railway.app/swagger-ui/index.html)

3. Fill out the pre-flight risk form

4. Watch the radar update in real-time

---

##📌 About the Project

The **SMS (Safety Management System) Tool** is a software solution developed to enhance the **Situational Awareness (SA)** of pilots and aviation managers before mission execution.

Unlike passive log systems, this application acts as an active safety barrier. Utilizing a **Quantitative Risk Matrix**, the system evaluates 20 critical points distributed across 4 fundamental pillars:

**Human Factors (Health)**

**Environment (Weather)**

**Machine (Aircraft)**

**Operation (Mission)**

The goal is to prevent, via software, high-risk operations from being initiated without a formalized Mitigation Plan, ensuring compliance with international flight safety standards.

---

##🚀 Technologies Used

**Backend:** Java 17, Spring Boot 3, Spring Data JPA, Hibernate, Maven.

**Database:** PostgreSQL 15 (Containerized).

**Frontend:** HTML5, Tailwind CSS, JavaScript (ES6+), Chart.js (Radar & Doughnut Charts).

**DevOps:** Docker, Docker Compose, GitHub Actions (CI/CD Pipeline).

**Testing:** JUnit 5, Mockito.

---

##💻 Code Examples (Technical Highlights)

Below are selected snippets demonstrating the implementation of critical business rules and software quality assurance.

### 1. Risk Evaluation Logic (Backend)
The `processRiskAndSave` method in `SafetyService` acts as the "Gatekeeper". It calculates the sum of risk factors and enforces the business rule: **High Risks (HIGH/NO_GO) require a mandatory mitigation plan.**

Java
// src/main/java/.../service/SafetyService.java

private SafetyEvaluation processRiskAndSave(SafetyEvaluation evaluation) {
    RiskLevel calculatedLevel;

    // 1. Kill Item: If the aircraft reaches a critical limit, the flight is NO_GO.
    if (evaluation.getAircraftScore() >= 50) {
        calculatedLevel = RiskLevel.NO_GO;
    } else {
        // 2. Sum of Factors Calculation
        int sum = evaluation.getHealthScore() + evaluation.getWeatherScore() +
                   evaluation.getAircraftScore() + evaluation.getMissionScore();

        if (sum <= THRESHOLD_LOW) calculatedLevel = RiskLevel.LOW;
        else if (sum < THRESHOLD_MEDIUM) calculatedLevel = RiskLevel.MEDIUM;
        else calculatedLevel = RiskLevel.HIGH;
    }

    // 3. Safety Lock (Critical Business Rule)
    boolean criticalRisk = (calculatedLevel == RiskLevel.HIGH || calculatedLevel == RiskLevel.NO_GO);
    boolean noMitigation = (evaluation.getMitigationPlan() == null || evaluation.getMitigationPlan().trim().isEmpty());

    // Prevents database persistence if the rule is violated
    if (criticalRisk && noMitigation) {
        throw new SafetyValidationException("ALERT: High-risk operations require a detailed Mitigation Plan.");
    }

    evaluation.setRiskLevel(calculatedLevel);
    return repository.save(evaluation);
    }

### 2. Unit Testing and Reliability
To ensure the "Safety Lock" never fails in production, we use automated tests with JUnit 5 and Mockito. The CI/CD pipeline fails if this test does not pass.

Java
// src/test/java/.../SafetyServiceTest.java

@Test
@DisplayName("Should throw exception (BLOCK) when risk is HIGH without mitigation")
void shouldBlockHighRiskWithoutMitigation() {
    // Scenario: Sum of scores = 60 (HIGH Risk)
    SafetyEvaluation evaluation = new SafetyEvaluation();
    evaluation.setHealthScore(15);
    evaluation.setWeatherScore(15);
    evaluation.setAircraftScore(15);
    evaluation.setMissionScore(15);
    evaluation.setMitigationPlan(""); // Purposely empty field

    // Action & Verification: The system must throw an error and NOT call the repository
    assertThrows(SafetyValidationException.class, () -> {
        safetyService.saveEvaluation(evaluation);
    });

    verify(safetyRepository, never()).save(any());
    }

### 3. Visual Intelligence (Frontend)
On the frontend, we use Chart.js to plot Situational Awareness in real-time. The logic below dynamically changes the radar color (Green/Yellow/Red) as the team's average readiness drops.

JavaScript
// index.html (Rendering Logic)

function updateRadar() {
    // Normalizes scores based on items marked in the checklist
    const scores = checklistData.map(cat => {
        const okItems = cat.items.filter(it => state.itemStates[it.id] === 'checked').length;
        return Math.round((okItems / cat.items.length) * 100);
    });
    
    // Average Readiness Calculation
    const avg = scores.reduce((a,b)=>a+b,0)/4;
    let color = '#0ea5e9'; // Blue (Default)

    // Traffic Light Logic for Visual Alert
    if(avg < 40) color = '#ef4444';      // Red (Critical)
    else if(avg < 75) color = '#f59e0b'; // Yellow (Caution)
    
    // Update Chart
    saRadarChart.data.datasets[0].borderColor = color;
    saRadarChart.data.datasets[0].backgroundColor = color + '33'; // Transparency
    saRadarChart.update();
    }

---

##📦 How to Run the Application

The application is fully "Dockerized" to facilitate deployment.

Prerequisites
Docker & Docker Compose installed.

Step-by-Step
Clone the repository:

```Bash
git clone https://github.com/GuerraMath/safety-api.git
cd safety-api
Start the environment (Database + API):
```
```Bash
docker-compose up -d
Access the Dashboard: Open your browser at: http://localhost:8081
```
---

##👤 Author

// Matheus Guerra

Master in Aviation Safety and Continued Airworthiness (ITA - Technological Institute of Aeronautics).

Civil Aviation Pilot and Instructor.

Software Developer focused on Critical Systems (Java/Spring).
