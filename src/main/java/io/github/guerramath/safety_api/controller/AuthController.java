package io.github.guerramath.safety_api.controller;

import io.github.guerramath.safety_api.dto.auth.*;
import io.github.guerramath.safety_api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/google")
    public ResponseEntity<AuthResponse> googleSignIn(@Valid @RequestBody GoogleSignInRequest request) {
        return ResponseEntity.ok(authService.googleSignIn(request));
    }
}