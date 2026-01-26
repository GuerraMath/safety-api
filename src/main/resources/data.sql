-- Cenário 1: Voo de Instrução Matinal (Risco Baixo)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 1, 1, 1, 1, 'Voo local, CAVOK, aeronave nominal.', 'BAIXO', CURRENT_TIMESTAMP);

-- Cenário 2: Traslado com Meteorologia Marginal (Risco Médio)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 2, 3, 1, 2, 'Alternativa próxima definida, combustível extra.', 'MEDIO', CURRENT_TIMESTAMP);

-- Cenário 3: Missão Crítica com Fadiga (Risco Alto - Mitigado)
INSERT INTO safety_evaluations (pilot_name, health_score, weather_score, aircraft_score, mission_score, mitigation_plan, risk_level, created_at)
VALUES ('Matheus Guerra', 4, 2, 1, 4, 'Presença de co-piloto experiente e paradas intermediárias.', 'ALTO', CURRENT_TIMESTAMP);