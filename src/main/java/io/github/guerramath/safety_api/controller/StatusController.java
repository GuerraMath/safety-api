package io.github.guerramath.safety_api.controller; // Verifique se o pacote bate com sua pasta

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
@RequestMapping("/api/v1") // Define o prefixo da URL
public class StatusController {

    @GetMapping("/status") // Define o fim da URL: /api/v1/status
    public Map<String, String> getStatus() {
        return Map.of(
                "status", "Operacional",
                "sistema", "Safety Management System API",
                "mensagem", "Decolagem autorizada!"
        );
    }
}