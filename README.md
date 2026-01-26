Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

Projeto desenvolvido individualmente por Matheus Guerra, do levantamento de requisitos à implementação. Esta aplicação é uma solução Full Stack de missão crítica, desenvolvida para elevar o nível de segurança operacional na aviação. O sistema automatiza o gerenciamento de risco pré-voo, integrando pesquisas acadêmicas de Fatores Humanos com engenharia de software moderna.

🧠 Fundamentação Científica

A base lógica deriva de pesquisas em Segurança de Voo e Aeronavegabilidade Continuada realizadas no ITA. A ferramenta foca na Consciência Situacional (SA), estruturada nos três níveis de Endsley:

Percepção: Coleta de dados de Saúde, Clima e Aeronave.

Compreensão: Processamento do impacto desses fatores na operação.

Projeção: Cálculo automatizado do nível de risco para a missão.

🛠️ Sumário Técnico

Stack Tecnológica
Backend: Java 17, Spring Boot 3.4.2, Spring Data JPA.

Frontend: HTML5, Tailwind CSS, JavaScript (Async/Await), Chart.js.

Banco de Dados: PostgreSQL 15 (Docker).

CI/CD: GitHub Actions com execução automatizada de testes.

Arquitetura
O sistema utiliza uma arquitetura Monolítica Modular com API RESTful. A lógica de negócio é isolada na camada de serviço, garantindo que o cálculo de risco e as validações de SMS ocorram antes da persistência no banco.

Escolhas de Design
Fail-Safe: Uso de GlobalExceptionHandler para bloquear registros de risco ALTO sem mitigação (Retorno 400 Bad Request).

Visualização Crítica: Gráfico radar para identificação imediata de pilares de risco degradados.

Infraestrutura como Código: Configuração de banco de dados e ambiente de CI via Docker e YAML.

🔧 Execução Local (Checklist de Partida)

Clonar Projeto:

Bash
git clone https://github.com/guerramath/safety-api.git
cd safety-api
Subir Infraestrutura:

Bash
docker-compose up -d
Rodar Aplicação:

Bash
./mvnw spring-boot:run
Acessar:

Dashboard: Abrir src/main/resources/static/index.html.

Documentação: http://localhost:8081/swagger-ui/index.html.

📡 Exemplos de Teste (cURL)
Simular Risco Baixo (Sucesso):

Bash
curl -X POST http://localhost:8081/api/v1/safety \
-H "Content-Type: application/json" \
-d '{
  "pilotName": "Matheus Guerra",
  "healthScore": 1,
  "weatherScore": 1,
  "aircraftScore": 1,
  "missionScore": 1,
  "mitigationPlan": "Voo Nominal"
}'
Simular Risco Alto sem Mitigação (Bloqueio):

Bash
curl -X POST http://localhost:8081/api/v1/safety \
-H "Content-Type: application/json" \
-d '{
  "pilotName": "Matheus Guerra",
  "healthScore": 5,
  "weatherScore": 5,
  "aircraftScore": 5,
  "missionScore": 5,
  "mitigationPlan": ""
}'

🗺️ Roadmap de Evolução

[ ] Integração Meteorológica: Consumo automático de METAR/TAF via API da NOAA.

[ ] Segurança: Implementação de autenticação JWT para diferentes níveis de acesso (Piloto/Auditor).

[ ] Relatórios: Exportação de histórico de segurança em formato PDF/CSV para auditorias de SMS.

👨‍✈️ Sobre o Autor

Matheus Guerra Mestre em Segurança e Aeronavegabilidade Continuada (ITA). Piloto e Instrutor de Aviação Civil. Atualmente focado em unir a experiência operacional aeronáutica com o desenvolvimento de software para criar sistemas de missão crítica mais seguros.

⚖️ Licença

Distribuído sob a Licença MIT. Veja o arquivo LICENSE para mais detalhes.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/guerramatheus)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/GuerraMath)
