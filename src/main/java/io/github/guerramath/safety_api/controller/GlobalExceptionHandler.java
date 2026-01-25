package io.github.guerramath.safety_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice // <--- Isso diz ao Spring: "Olhe todos os erros de todos os Controllers"
public class GlobalExceptionHandler {

    // Intercepta o erro que lançamos no Service quando a mitigação falta
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleSafetyException(RuntimeException ex, WebRequest request) {

        // Montamos um JSON de resposta amigável
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value()); // Erro 400 (Erro do Cliente/Piloto)
        body.put("error", "Bloqueio de Segurança Operacional");
        body.put("message", ex.getMessage()); // A mensagem "ALERTA DE SEGURANÇA..." vem aqui


        // Retorna HTTP 400 (Bad Request) em vez de 500
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }
}