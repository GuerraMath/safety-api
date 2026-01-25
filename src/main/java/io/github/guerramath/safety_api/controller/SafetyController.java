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
}