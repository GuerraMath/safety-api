package io.github.guerramath.safety_api.controller;

import io.github.guerramath.safety_api.dto.SafetyAssessmentDTO;
import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.service.SafetyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/safety")
public class SafetyController {

    private final SafetyService service;

    public SafetyController(SafetyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<SafetyEvaluation> criarAvaliacao(@RequestBody SafetyAssessmentDTO dto) {
        SafetyEvaluation novaAvaliacao = service.saveEvaluation(dto);
        return ResponseEntity.ok(novaAvaliacao);
    }

    @GetMapping("/history")
    public ResponseEntity<List<SafetyEvaluation>> getHistory() {
        return ResponseEntity.ok(service.getHistory());
    }

    @GetMapping("/export")
    public ResponseEntity<String> exportAuditReport() {
        List<SafetyEvaluation> history = service.getHistory();
        StringBuilder csv = new StringBuilder();
        csv.append("ID;Data_Hora;Piloto;Risco;Saude;Clima;Aeronave;Missao\n");

        for (SafetyEvaluation eval : history) {
            csv.append(eval.getId()).append(";")
                    .append(eval.getDateTime()).append(";")
                    .append(eval.getPilotName()).append(";")
                    .append(eval.getRiskLevel()).append(";")
                    .append(eval.getHealthScore()).append(";")
                    .append(eval.getWeatherScore()).append(";")
                    .append(eval.getAircraftScore()).append(";")
                    .append(eval.getMissionScore()).append("\n");
        }

        return ResponseEntity.ok()
                .header("Content-Type", "text/csv")
                .header("Content-Disposition", "attachment; filename=auditoria_sms.csv")
                .body(csv.toString());
    }
}