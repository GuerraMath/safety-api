##Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

Projeto desenvolvido individualmente, do levantamento de requisitos à implementação backend e frontend. Esta aplicação é uma solução Full Stack de missão crítica, desenvolvida para elevar o nível de segurança operacional na aviação. O sistema automatiza o gerenciamento de risco pré-voo, integrando pesquisas acadêmicas de Fatores Humanos com engenharia de software moderna.

##🧠 Fundamentação Científica

A base lógica deriva de pesquisas em Segurança de Voo e Aeronavegabilidade Continuada (Mestrado - ITA). A ferramenta foca na Consciência Situacional (SA), estruturada nos três níveis de Endsley:

Percepção: Coleta de dados (Saúde, Clima, Aeronave).

Compreensão: Processamento do impacto desses fatores na operação.

Projeção: Cálculo automatizado do nível de risco para a missão.

##🛠️ Sumário Técnico

Stack Tecnológica
Backend: Java 17, Spring Boot 3.4.2, Spring Data JPA, Hibernate.

Frontend: HTML5, Tailwind CSS, JavaScript (Async/Await), Chart.js.

Banco de Dados: PostgreSQL (executando via Docker).

Documentação: Swagger/OpenAPI 3.

Arquitetura
O sistema utiliza uma arquitetura Monolítica Modular baseada em API RESTful. O fluxo de dados separa rigorosamente a lógica de cálculo de risco (Service Layer) da persistência de dados, garantindo que as regras de segurança operacional sejam validadas antes de qualquer registro no banco.

Escolhas de Design
Fail-Safe Operacional: Bloqueio via GlobalExceptionHandler que interrompe missões de risco ALTO sem mitigação (400 Bad Request).

UX de Aviação: Gráficos radar para identificação imediata de degradação de pilares de segurança.

Infraestrutura Imutável: Ambiente de dados replicável via Docker Compose.

##🔧 Execução Local (Checklist de Partida)

Pré-requisitos
Java 17 | Docker | Maven (opcional, use o ./mvnw incluso).

Passo a Passo
Clone e Acesse:

Bash
git clone https://github.com/guerramath/safety-api.git
cd safety-api
Subir Banco de Dados:

Bash
docker-compose up -d
Rodar API:

Bash
./mvnw spring-boot:run
Acessar:

Interface: Abra index.html no navegador.

Swagger: http://localhost:8081/swagger-ui/index.html.

##📡 Testando a API

Exemplo via cURL (Registro de Risco)
Bash
curl -X POST http://localhost:8081/api/v1/safety \
-H "Content-Type: application/json" \
-d '{
  "pilotName": "Matheus Guerra",
  "healthScore": 1,
  "weatherScore": 2,
  "aircraftScore": 1,
  "missionScore": 1,
  "mitigationPlan": "Operação Nominal"
}'
Postman Collection
Importe o JSON abaixo no Postman para ter os endpoints prontos:

<details> <summary>Clique para expandir o JSON</summary>

JSON
{
	"info": {
		"name": "Safety API SMS",
		"schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
	},
	"item": [
		{
			"name": "Registrar Avaliação",
			"request": {
				"method": "POST",
				"header": [],
				"body": {
					"mode": "raw",
					"raw": "{\n  \"pilotName\": \"Matheus Guerra\",\n  \"healthScore\": 1,\n  \"weatherScore\": 1,\n  \"aircraftScore\": 1,\n  \"missionScore\": 1,\n  \"mitigationPlan\": \"Nenhum risco detectado\"\n}",
					"options": { "raw": { "language": "json" } }
				},
				"url": { "raw": "http://localhost:8081/api/v1/safety" }
			}
		},
		{
			"name": "Listar Histórico",
			"request": {
				"method": "GET",
				"header": [],
				"url": { "raw": "http://localhost:8081/api/v1/safety/history" }
			}
		}
	]
}
</details>

##🗺️ Roadmap e Issues

Próximos Passos
[ ] Integração com APIs Meteorológicas (NOAA/METAR).

[ ] Autenticação via Spring Security + JWT.

[ ] Dashboard Mobile com React Native (Offline-first).

Como Contribuir
Abra uma Issue relatando o bug ou sugestão.

Faça um Fork do projeto.

Crie uma branch (git checkout -b feature/nova-melhoria).

Envie um Pull Request.

##👨‍✈️ Sobre o Autor

Matheus Guerra – Mestre em Segurança e Aeronavegabilidade Continuada pelo ITA. Piloto e Instrutor de Aviação Civil, unindo bagagem técnica aeronáutica com engenharia de software.

[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://linkedin.com/in/guerramatheus)
[![GitHub](https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white)](https://github.com/GuerraMath)
