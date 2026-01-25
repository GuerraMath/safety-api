# Safety Management System (SMS) - Situational Awareness Tool 🛫🛡️

Esta aplicação é uma solução **Full Stack** desenvolvida para elevar o nível de segurança operacional na aviação. O sistema automatiza o gerenciamento de risco pré-voo, transformando conceitos complexos de **Fatores Humanos** em dados acionáveis através de uma API REST e uma interface interativa.

## 🧠 Fundamentação Científica

A base lógica deriva de pesquisas em **Segurança de Voo e Aeronavegabilidade Continuada (Mestrado - ITA)**. A ferramenta foca na **Consciência Situacional (SA)**, estruturada nos três níveis de Endsley:
1. **Percepção**: Coleta de dados (Saúde, Clima, Aeronave).
2. **Compreensão**: Processamento do impacto desses fatores.
3. **Projeção**: Cálculo automatizado do nível de risco para a missão.

---

## 🚀 Tecnologias e Infraestrutura

### Backend (O Motor de Decisão)

* **Java 17 & Spring Boot 3.4.2**: Core estável para sistemas de missão crítica.
* **Spring Data JPA**: Abstração de persistência e regras de negócio.
* **PostgreSQL (Docker)**: Banco de dados relacional rodando em container para isolamento e portabilidade.
* **Swagger/OpenAPI 3**: Documentação interativa para auditoria de endpoints.

### Frontend (O Painel de Instrumentos)

* **HTML5 & Tailwind CSS**: Interface responsiva com foco na experiência do piloto.
* **Chart.js**: Feedback visual via gráfico radar para prontidão operacional.
* **JavaScript (Async/Await)**: Comunicação assíncrona com a API REST.

---

## 🏗️ Arquitetura do Sistema

O projeto segue o padrão de camadas para facilitar a escalabilidade e manutenção:
* **Model**: Entidades de segurança (Assessments).
* **Service**: O "Cérebro" onde as regras de Fatores Humanos aplicam o cálculo de `riskLevel`.
* **Controller**: Endpoints REST documentados via Swagger.
* **Repository**: Comunicação persistente com o PostgreSQL.

---

## 🛠️ Funcionalidades Principais

* **Checklist Interativo**: Avaliação baseada em 4 pilares (Saúde, Clima, Aeronave e Missão).
* **Cálculo de Risco em Tempo Real**: Diagnóstico automatizado (BAIXO, MÉDIO, ALTO).
* **Bloqueio de Segurança**: Impede o registro de missões críticas sem mitigação (Conformidade SMS).
* **Persistência de Histórico**: Registro rastreável para análise de tendências e auditorias.

---

## 🔧 Execução via Docker (Procedimento Rápido)

1. **Subir Infraestrutura**: `docker-compose up -d` (PostgreSQL na porta 5433).
2. **Rodar API**: `./mvnw spring-boot:run` (Porta 8081).
3. **Documentação**: Acesse `/swagger-ui/index.html` para testes interativos.

---

## 👨‍✈️ Sobre o Autor

**Matheus Guerra** Mestre em Segurança e Aeronavegabilidade Continuada pelo **ITA**. Piloto e Instrutor de Aviação Civil, unindo a experiência de cockpit com a engenharia de software para criar soluções que salvam vidas.
