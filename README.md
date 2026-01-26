Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️
Projeto desenvolvido individualmente, do levantamento de requisitos à implementação backend e frontend. Esta aplicação é uma solução Full Stack de missão crítica, desenvolvida para elevar o nível de segurança operacional na aviação. O sistema automatiza o gerenciamento de risco pré-voo, integrando pesquisas acadêmicas de Fatores Humanos com engenharia de software moderna.

🧠 Fundamentação Científica
A base lógica deriva de pesquisas em Segurança de Voo e Aeronavegabilidade Continuada (Mestrado - ITA). A ferramenta foca na Consciência Situacional (SA), estruturada nos três níveis de Endsley:

Percepção: Coleta de dados (Saúde, Clima, Aeronave).

Compreensão: Processamento do impacto desses fatores na operação.

Projeção: Cálculo automatizado do nível de risco para a missão.

🛠️ Sumário Técnico
Stack Tecnológica
Backend: Java 17, Spring Boot 3.4.2, Spring Data JPA, Hibernate.

Frontend: HTML5, Tailwind CSS, JavaScript (Async/Await), Chart.js (Gráficos Radar).

Banco de Dados: PostgreSQL (executando via Docker).

Documentação: Swagger/OpenAPI 3.

Arquitetura
O sistema utiliza uma arquitetura Monolítica Modular baseada em API RESTful. O fluxo de dados separa rigorosamente a lógica de cálculo de risco (Service Layer) da persistência de dados, garantindo que as regras de segurança operacional sejam validadas antes de qualquer registro no banco.

Principais Endpoints
POST /api/v1/safety: Processa scores e retorna o diagnóstico de risco.

GET /api/v1/safety/history: Recupera o histórico para auditoria e análise de tendências.

GET /swagger-ui/index.html: Documentação interativa para testes de contrato.

Escolhas de Design
Fail-Safe Operacional: Implementação de um GlobalExceptionHandler que atua como barreira de segurança. Caso o sistema detecte risco ALTO sem um plano de mitigação, a transação é interrompida com um 400 Bad Request.

UX Baseada em Consciência Situacional: O uso do gráfico radar no frontend permite que o piloto identifique instantaneamente qual pilar de segurança (ex: fadiga ou meteorologia) está degradando a operação.

Infraestrutura Imutável: Uso de Docker Compose para garantir que o ambiente de banco de dados seja replicável e isolado.

🏗️ Funcionalidades Chave
Cálculo de Risco em Tempo Real: Diagnóstico automatizado (BAIXO, MÉDIO, ALTO) baseado em 4 pilares críticos.

Bloqueio de Segurança: Impede o registro de missões com risco crítico sem a devida mitigação, forçando a conformidade com o SMS.

Módulo de Auditoria: Geração de dados estruturados para relatórios técnicos e rastreabilidade para processos de aeronavegabilidade.

Persistência de Histórico: Registro completo para análise de segurança de voo a longo prazo.

🔧 Execução e Deploy (Checklist de Partida)
Subir Infraestrutura: docker-compose up -d (PostgreSQL na porta 5433).

Compilar e Rodar API: ./mvnw clean compile spring-boot:run (Porta 8081).

Interface e Documentação:

Painel do Piloto: Acesse o arquivo index.html.

Swagger UI: Acesse http://localhost:8081/swagger-ui/index.html.

🚀 Exemplos de Uso da API
1. Registrar Avaliação de Risco (POST)
Endpoint: POST http://localhost:8081/api/v1/safety

Cenário A: Voo Normal (Risco Baixo)
JSON
{
  "pilotName": "Cmte. Matheus Guerra",
  "healthScore": 1,
  "weatherScore": 1,
  "aircraftScore": 1,
  "missionScore": 1,
  "mitigationPlan": "Voo de teste nominal"
}
Resposta: 200 OK

Cenário B: Bloqueio de Segurança (Risco Alto SEM Mitigação)
JSON
{
  "pilotName": "Cmte. Matheus Guerra",
  "healthScore": 5,
  "weatherScore": 5,
  "aircraftScore": 5,
  "missionScore": 5,
  "mitigationPlan": ""
}
Resposta: 400 Bad Request

Mensagem: "ALERTA: Risco ALTO detectado. Informe o plano de mitigação para prosseguir."

👨‍✈️ Sobre o Autor
Matheus Guerra – Mestre em Segurança e Aeronavegabilidade Continuada pelo ITA. Piloto e Instrutor de Aviação Civil, unindo o domínio técnico aeronáutico com a engenharia de software para desenvolver soluções que salvam vidas.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/guerramatheus)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/GuerraMath)
