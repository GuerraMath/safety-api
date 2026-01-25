# Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

Projeto desenvolvido individualmente, do levantamento de requisitos à implementação backend e frontend. Esta aplicação é uma solução **Full Stack de missão crítica**, desenvolvida para elevar o nível de segurança operacional na aviação. O sistema automatiza o gerenciamento de risco pré-voo, integrando pesquisas acadêmicas de **Fatores Humanos** com engenharia de software moderna.

## 🧠 Fundamentação Científica
A base lógica deriva de pesquisas em **Segurança de Voo e Aeronavegabilidade Continuada (Mestrado - ITA)**. A ferramenta foca na **Consciência Situacional (SA)**, estruturada nos três níveis de Endsley:
* **Percepção**: Coleta de dados (Saúde, Clima, Aeronave).
* **Compreensão**: Processamento do impacto desses fatores na operação.
* **Projeção**: Cálculo automatizado do nível de risco para a missão.

---

## 🚀 Tecnologias e Infraestrutura

### Backend (O Motor de Decisão)
* **Java 17 & Spring Boot 3.4.2**: Core estável para sistemas de missão crítica.
* **Spring Data JPA**: Abstração de persistência e regras de negócio.
* **PostgreSQL (Docker)**: Banco de dados relacional rodando em container para isolamento e portabilidade (**Porta 5433**).
* **Swagger/OpenAPI 3**: Documentação interativa e auditoria de endpoints.

### Frontend (O Painel de Instrumentos)
* **HTML5 & Tailwind CSS**: Interface responsiva com foco na experiência do piloto.
* **Chart.js**: Feedback visual via gráfico radar (SA) e dashboard de distribuição de risco.
* **JavaScript (Async/Await)**: Comunicação assíncrona com a API REST.

---

## 🏗️ Arquitetura e Funcionalidades
* **Cálculo de Risco em Tempo Real**: Diagnóstico automatizado (BAIXO, MÉDIO, ALTO) baseado em 4 pilares críticos.
* **Bloqueio de Segurança**: Impede o registro de missões com risco crítico sem a devida mitigação, forçando a conformidade com o SMS (**Safety Management System**).
* **Módulo de Auditoria**: Geração de relatórios técnicos em formato **CSV**, permitindo rastreabilidade para processos de aeronavegabilidade continuada e auditorias.
* **Persistência de Histórico**: Registro completo para análise de tendências e segurança de voo.

---

## 🔧 Execução e Deploy (Checklist de Partida)

1.  **Subir Infraestrutura**: `docker-compose up -d` (PostgreSQL na porta 5433).
2.  **Compilar e Rodar API**: `./mvnw clean compile spring-boot:run` (Porta 8081).
3.  **Interface e Documentação**:
    * **Painel do Piloto**: Acesse o arquivo `index.html` (conectado à porta 8081).
    * **Swagger UI**: Acesse `http://localhost:8081/swagger-ui/index.html`.

---

## 🛠️ Exemplos de Uso da API (Endpoints)

Abaixo estão os principais cenários de teste para validação da lógica de segurança operacional.

### 1. Registrar Avaliação de Risco (POST)
**Endpoint:** `POST http://localhost:8081/api/v1/safety`

#### **Cenário A: Voo Normal (Risco Baixo)**
Simula uma operação onde todos os indicadores estão nominais.
* **Request Body:**
``json
{
  "pilotName": "Cmte. Matheus Guerra",
  "healthScore": 1,
  "weatherScore": 1,
  "aircraftScore": 1,
  "missionScore": 1,
  "mitigationPlan": "Voo de teste nominal"
}
Resposta Esperada: 200 OK

Cenário B: Risco Alto SEM Mitigação (Teste de Bloqueio)
Validação do GlobalExceptionHandler. O sistema impede o registro se o risco for alto e o plano de mitigação estiver ausente.

Request Body:

JSON
{
  "pilotName": "Cmte. Matheus Guerra",
  "healthScore": 5,
  "weatherScore": 5,
  "aircraftScore": 5,
  "missionScore": 5,
  "mitigationPlan": ""
}
Resposta Esperada: 400 Bad Request

Custom Error Response:

JSON
{
  "timestamp": "2026-01-25T...",
  "status": 400,
  "error": "Bloqueio de Segurança Operacional",
  "message": "ALERTA: Risco ALTO detectado. Informe o plano de mitigação para prosseguir."
}

2. Consulta de Histórico (GET)
Endpoint: GET http://localhost:8081/api/v1/safety/history Retorna todas as missões para análise de tendência (SMS).

---

## 👨‍✈️ Sobre o Autor

**Matheus Guerra** Mestre em Segurança e Aeronavegabilidade Continuada pelo ITA. Piloto e Instrutor de Aviação Civil, unindo a bagagem técnica aeronáutica com a engenharia de software para salvar vidas.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/guerramatheus)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/GuerraMath)
