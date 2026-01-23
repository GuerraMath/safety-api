🛫 Safety Management System (SMS) - Situational Awareness Tool

Este projeto é uma Single Page Application (SPA) integrada a uma API REST, desenvolvida para elevar o nível de segurança operacional na aviação agrícola. A ferramenta automatiza o gerenciamento de risco pré-voo, transformando conceitos complexos de Fatores Humanos em dados acionáveis.

🧠 Contexto e Fundamentação

A base lógica desta aplicação deriva de pesquisas acadêmicas em Segurança de Voo e Aeronavegabilidade Continuada (Mestrado - ITA). O foco central é a Consciência Situacional (SA), estruturada nos três níveis de Endsley:

Percepção: Coleta de dados de saúde, meteorologia e envelope.

Compreensão: Processamento do impacto desses fatores na operação.

Projeção: Cálculo automatizado do nível de risco para a missão.

🚀 Tecnologias Utilizadas

Backend (O Motor de Decisão)
Java 17 / Spring Boot 3: Estrutura robusta para sistemas de missão crítica.

Spring Data JPA: Abstração de persistência de dados.

PostgreSQL: Banco de dados relacional para garantir a integridade e rastreabilidade das missões.

Spring Security (CORS): Configuração de segurança para integração entre domínios.

Frontend (O Painel de Instrumentos)
HTML5 / Tailwind CSS: Interface responsiva e moderna.

Chart.js: Visualização de dados através de gráfico radar para prontidão operacional.

JavaScript (Async/Await): Comunicação assíncrona com a API.

🏗️ Arquitetura do Sistema

O projeto segue o padrão de camadas para facilitar a manutenção e escalabilidade:

Model: Representação das entidades de segurança (Assessments).

Repository: Interface de comunicação imutável com o banco de dados.

Service: O "Cérebro" do sistema, onde as regras de Fatores Humanos são aplicadas para calcular o riskLevel.

Controller: Endpoints REST que expõem as funcionalidades de registro e histórico.

🛠️ Funcionalidades Principais

Checklist Interativo: Avaliação baseada em 4 pilares críticos (Saúde, Clima, Envelope e Gerenciamento de Risco).

Cálculo de Risco em Tempo Real: O backend processa as variáveis e retorna um diagnóstico (BAIXO, MÉDIO, ALTO).

Histórico de Missões: Registro persistente para análise de tendências e auditorias de segurança (SMS).

Gráfico Radar de SA: Feedback visual imediato para o piloto sobre sua prontidão.

🔧 Configuração e Execução

Clone o repositório: git clone ...

Configure o banco de dados: Crie um banco safety_db no PostgreSQL.

Ajuste o application.properties: Insira suas credenciais do banco.

Execute o Backend: Rode a classe SafetyChecklistApiApplication.

Acesse o Frontend: Abra o index.html localmente ou via servidor web.

👨‍✈️ Sobre o Autor

Matheus Guerra Mestre em Segurança de Voo e Aeronavegabilidade Continuada pelo ITA. Piloto e Instrutor de Aviação Civil, atualmente unindo a bagagem técnica do setor aeronáutico com a engenharia de software para criar soluções que salvam vidas e otimizam operações críticas.
