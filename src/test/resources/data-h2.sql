-- Usuarios de teste para H2 (senha: password123)
-- BCrypt hash: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36E0MYzT
INSERT INTO users (name, email, password_hash, role, auth_provider, email_verified, created_at, updated_at)
VALUES ('Test User', 'test@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36E0MYzT', 'PILOT', 'LOCAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO users (name, email, password_hash, role, auth_provider, email_verified, created_at, updated_at)
VALUES ('Admin User', 'admin@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcg7b3XeKeUxWdeS86E36E0MYzT', 'ADMIN', 'LOCAL', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Cenário 1: Voo de Instrução Matinal (Risco Baixo)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 1, 1, 1, 1, 'Voo local, CAVOK, aeronave nominal.', 'LOW', CURRENT_TIMESTAMP);

-- Cenário 2: Traslado com Meteorologia Marginal (Risco Médio)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 2, 3, 1, 2, 'Alternativa próxima definida, combustível extra.', 'MEDIUM', CURRENT_TIMESTAMP);

-- Cenário 3: Missão Crítica com Fadiga (Risco Alto - Mitigado)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 4, 2, 1, 4, 'Presença de co-piloto experiente e paradas intermediárias.', 'HIGH', CURRENT_TIMESTAMP);
