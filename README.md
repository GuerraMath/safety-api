Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-green?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?style=for-the-badge&logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker)
![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen?style=for-the-badge)
![CI](https://github.com/GuerraMath/safety-api/actions/workflows/ci.yml/badge.svg)

## 📌 Sobre o Projeto

O **SMS (Safety Management System) Tool** é uma solução de software desenvolvida para elevar a **Consciência Situacional (SA)** de pilotos e gestores de aviação antes da execução de missões.

Diferente de sistemas de log passivos, esta aplicação atua como uma barreira de segurança ativa. Utilizando uma **Matriz de Risco Quantitativa**, o sistema avalia 20 pontos críticos distribuídos em 4 pilares fundamentais:
1.  **Fatores Humanos (Saúde)**
2.  **Ambiente (Meteorologia)**
3.  **Máquina (Aeronave)**
4.  **Operação (Missão)**

O objetivo é impedir, via software, que operações de risco elevado sejam iniciadas sem um **Plano de Mitigação** formalizado, garantindo conformidade com normas internacionais de segurança de voo.

---

## 🚀 Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Hibernate, Maven.
* **Database:** PostgreSQL 15 (Containerizado).
* **Frontend:** HTML5, Tailwind CSS, JavaScript (ES6+), Chart.js (Radar & Doughnut Charts).
* **DevOps:** Docker, Docker Compose, GitHub Actions (CI/CD Pipeline).
* **Testing:** JUnit 5, Mockito.

---

## 💻 Exemplos de Código (Technical Highlights)

Abaixo estão trechos selecionados que demonstram a implementação das regras de negócio críticas e a garantia de qualidade do software.

### 1. Lógica de Avaliação de Risco (Backend)
O método `processRiskAndSave` no `SafetyService` atua como o "Gatekeeper". Ele calcula a soma dos fatores de risco e impõe a regra de negócio: **Riscos Elevados (HIGH/NO_GO) exigem mitigação obrigatória.**

java
// src/main/java/.../service/SafetyService.java

private SafetyEvaluation processRiskAndSave(SafetyEvaluation evaluation) {
    RiskLevel nivelCalculado;

    // 1. Kill Item: Se a aeronave atinge o limite crítico, o voo é NO_GO.
    if (evaluation.getAircraftScore() >= 50) {
        nivelCalculado = RiskLevel.NO_GO;
    } else {
        // 2. Cálculo da Soma dos Fatores
        int soma = evaluation.getHealthScore() + evaluation.getWeatherScore() +
                   evaluation.getAircraftScore() + evaluation.getMissionScore();

        if (soma <= THRESHOLD_LOW) nivelCalculado = RiskLevel.LOW;
        else if (soma < THRESHOLD_MEDIUM) nivelCalculado = RiskLevel.MEDIUM;
        else nivelCalculado = RiskLevel.HIGH;
    }

    // 3. Trava de Segurança (Regra de Negócio Crítica)
    boolean riscoCritico = (nivelCalculado == RiskLevel.HIGH || nivelCalculado == RiskLevel.NO_GO);
    boolean semMitigacao = (evaluation.getMitigationPlan() == null || evaluation.getMitigationPlan().trim().isEmpty());

    // Impede a persistência no banco se a regra for violada
    if (riscoCritico && semMitigacao) {
        throw new SafetyValidationException("ALERTA: Operações de risco elevado exigem Plano de Mitigação detalhado.");
    }

    evaluation.setRiskLevel(nivelCalculado);
    return repository.save(evaluation);
    }

### 2. Testes Unitários e Confiabilidade
Para garantir que a "Trava de Segurança" nunca falhe em produção, utilizamos testes automatizados com JUnit 5 e Mockito. O pipeline de CI/CD falha se este teste não passar.

Java
// src/test/java/.../SafetyServiceTest.java

@Test
@DisplayName("Deve lançar exceção (BLOQUEIO) quando risco é ALTO sem mitigação")
void deveBloquearRiscoAltoSemMitigacao() {
    // Cenário: Soma de scores = 60 (Risco HIGH)
    SafetyEvaluation evaluation = new SafetyEvaluation();
    evaluation.setHealthScore(15);
    evaluation.setWeatherScore(15);
    evaluation.setAircraftScore(15);
    evaluation.setMissionScore(15);
    evaluation.setMitigationPlan(""); // Campo vazio propositalmente

    // Ação & Verificação: O sistema deve lançar erro e NÃO chamar o repositório
    assertThrows(SafetyValidationException.class, () -> {
        safetyService.saveEvaluation(evaluation);
    });

    verify(safetyRepository, never()).save(any());
    }

### 3. Inteligência Visual (Frontend)

No Frontend, utilizamos Chart.js para plotar a Consciência Situacional em tempo real. A lógica abaixo altera a cor do radar dinamicamente (Verde/Amarelo/Vermelho) conforme a prontidão média da equipe cai.

JavaScript
// index.html (Lógica de Renderização)

function updateRadar() {
    // Normaliza os scores baseados nos itens marcados no checklist
    const scores = checklistData.map(cat => {
        const okItems = cat.items.filter(it => state.itemStates[it.id] === 'checked').length;
        return Math.round((okItems / cat.items.length) * 100);
    });
    
    // Cálculo da Prontidão Média
    const avg = scores.reduce((a,b)=>a+b,0)/4;
    let color = '#0ea5e9'; // Azul (Padrão)

    // Lógica de Semáforo para Alerta Visual
    if(avg < 40) color = '#ef4444';      // Vermelho (Crítico)
    else if(avg < 75) color = '#f59e0b'; // Amarelo (Atenção)
    
    // Atualização do Gráfico
    saRadarChart.data.datasets[0].borderColor = color;
    saRadarChart.data.datasets[0].backgroundColor = color + '33'; // Transparência
    saRadarChart.update();
    }

📦 Como Rodar a Aplicação

A aplicação foi totalmente "Dockerizada" para facilitar o deploy.

Pré-requisitos
Docker & Docker Compose instalados.

Passo a Passo
Clone o repositório:

Bash
git clone [https://github.com/GuerraMath/safety-api.git](https://github.com/GuerraMath/safety-api.git)
cd safety-api
Suba o ambiente (Database + API):

Bash
docker-compose up -d
Acesse o Dashboard: Abra seu navegador em: http://localhost:8081

👤 Autor

Matheus Guerra

Mestre em Segurança de Aviação e Aeronavegabilidade Continuada (ITA - Instituto Tecnológico de Aeronáutica).

Piloto e Instrutor de Aviação Civil.

Desenvolvedor de Software com foco em Sistemas Críticos (Java/Spring).

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/guerramatheus)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/GuerraMath)
