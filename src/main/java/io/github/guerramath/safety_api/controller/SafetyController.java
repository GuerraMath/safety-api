package io.github.guerramath.safety_api.controller;

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

    @PostMapping("/check")
    public SafetyAssessment registrar(@RequestBody SafetyAssessment assessment) {
        return service.salvarAvaliacao(assessment);
    }

    @GetMapping("/history")
    public List<SafetyAssessment> getHistory() {
        return service.buscarTodos();
    }
}