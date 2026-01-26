🛠️ Guia de Execução Local
Pré-requisitos
Java 17 instalado.

Docker e Docker Compose (para o banco de dados).

Maven (opcional, o repositório inclui o mvnw).

Passo a Passo
Clone o repositório:

Bash
git clone https://github.com/guerramath/safety-api.git
cd safety-api
Inicie o banco de dados (PostgreSQL):

Bash
docker-compose up -d
O banco estará disponível na porta 5433 (conforme definido no seu docker-compose.yml).

Execute a aplicação:

Bash
./mvnw spring-boot:run
A API iniciará em http://localhost:8081.

📡 Exemplos de Teste (cURL)
Você pode testar os endpoints diretamente do terminal:

1. Registrar Missão (Sucesso):

Bash
curl -X POST http://localhost:8081/api/v1/safety \
-H "Content-Type: application/json" \
-d '{
"pilotName": "Matheus Guerra",
"healthScore": 2,
"weatherScore": 2,
"aircraftScore": 1,
"missionScore": 1,
"mitigationPlan": "Operação nominal sob VFR"
}'
2. Listar Histórico:

Bash
curl -X GET http://localhost:8081/api/v1/safety/history
📂 Postman Collection
Para facilitar, copie o JSON abaixo, salve como safety-api.postman_collection.json e importe no seu Postman:

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
🐛 Como Reportar Issues
Encontrou um bug ou tem uma sugestão de melhoria? Siga este padrão:

Vá na aba Issues do repositório.

Clique em New Issue.

Título: Use prefixos como [BUG] ou [FEATURE].

Descrição:

O que aconteceu?

Como reproduzir?

Qual o comportamento esperado?