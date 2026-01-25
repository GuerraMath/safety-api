package io.github.guerramath.safety_api.controller;

import org.springframework.http.ResponseEntity;
import io.github.guerramath.safety_api.dto.SafetyAssessmentDTO;
import io.github.guerramath.safety_api.model.SafetyAssessment;
import io.github.guerramath.safety_api.service.SafetyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/safety")
public class SafetyController {

    @Autowired
    private SafetyService service;

    @PostMapping
    public ResponseEntity<SafetyAssessment> criarAvaliacao(@RequestBody SafetyAssessmentDTO dto) {
        SafetyAssessment novaAvaliacao = service.salvarAvaliacao(dto);
        return ResponseEntity.ok(novaAvaliacao);
    }

    @GetMapping("/history")
    public List<SafetyAssessment> getHistory() {
        return service.buscarTodos();
    }
    @GetMapping("/export")
    public ResponseEntity<String> exportAuditReport() {
        // Agora o service.getHistory() será reconhecido
        List<SafetyAssessment> history = service.getHistory();

        StringBuilder csv = new StringBuilder();
        // Cabeçalho ajustado para os padrões de SMS
        csv.append("ID;Data_Hora;Piloto;Risco;Saude;Clima;Aeronave;Missao\n");

        for (SafetyAssessment v : history) {
            csv.append(v.getId()).append(";")
                    .append(v.getDateTime()).append(";")
                    .append(v.getPilotName()).append(";")
                    .append(v.getRiskLevel()).append(";")
                    .append(v.getHealthScore()).append(";")
                    .append(v.getWeatherScore()).append(";")
                    .append(v.getAircraftScore()).append(";")
                    .append(v.getMissionScore()).append("\n");
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=relatorio_auditoria_safety.csv")
                .header("Content-Type", "text/csv; charset=UTF-8")
                .body(csv.toString());
    }
}