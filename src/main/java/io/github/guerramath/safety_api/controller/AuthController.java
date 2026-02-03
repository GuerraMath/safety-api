package io.github.guerramath.safety_api.controller;

import io.github.guerramath.safety_api.dto.auth.AuthResponse;
import io.github.guerramath.safety_api.dto.auth.ForgotPasswordRequest;
import io.github.guerramath.safety_api.dto.auth.GoogleSignInRequest;
import io.github.guerramath.safety_api.dto.auth.LoginRequest;
import io.github.guerramath.safety_api.dto.auth.MessageResponse;
import io.github.guerramath.safety_api.dto.auth.RefreshTokenRequest;
import io.github.guerramath.safety_api.dto.auth.RegisterRequest;
import io.github.guerramath.safety_api.dto.auth.UserDto;
import io.github.guerramath.safety_api.service.AuthService;
import io.github.guerramath.safety_api.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller para endpoints de autenticacao.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacao", description = "Endpoints de autenticacao")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    @Operation(summary = "Login com email e senha")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar novo usuario")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google")
    @Operation(summary = "Login/Registro via Google")
    public ResponseEntity<AuthResponse> googleSignIn(@Valid @RequestBody GoogleSignInRequest request) {
        AuthResponse response = authService.googleSignIn(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Atualizar token de acesso")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        AuthResponse response = authService.refreshToken(request.getRefreshToken());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Solicitar recuperacao de senha")
    public ResponseEntity<MessageResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {
        // TODO: Implementar envio de email
        return ResponseEntity.ok(
                new MessageResponse("Se o email estiver cadastrado, voce recebera instrucoes de recuperacao."));
    }

    @GetMapping("/me")
    @Operation(summary = "Obter dados do usuario autenticado")
    public ResponseEntity<UserDto> getCurrentUser(
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(401).build();
        }

        String userId = jwtService.extractUserId(token);
        UserDto user = authService.getCurrentUser(Long.parseLong(userId));
        return ResponseEntity.ok(user);
    }

    @PostMapping("/logout")
    @Operation(summary = "Fazer logout")
    public ResponseEntity<Void> logout() {
        // Logout eh tratado no cliente removendo os tokens
        // O servidor nao precisa fazer nada especial para JWT stateless
        return ResponseEntity.ok().build();
    }
}
