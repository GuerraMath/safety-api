package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.dto.auth.*;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.AuthProvider;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.model.UserRole;
import io.github.guerramath.safety_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final GoogleAuthService googleAuthService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        if (user.getAuthProvider() != AuthProvider.LOCAL) {
            throw new AuthException("Esta conta utiliza autenticação via " + user.getAuthProvider());
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException("Senha incorreta");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email ja cadastrado");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.PILOT)
                .authProvider(AuthProvider.LOCAL)
                .build();

        user = userRepository.save(user);
        return buildAuthResponse(user);
    }

    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new AuthException("Token inválido ou expirado");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new AuthException("Token não é um refresh token");
        }

        String userIdStr = jwtService.extractUserId(refreshToken);
        Long userId = Long.parseLong(userIdStr);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));

        return buildAuthResponse(user);
    }

    public UserDto getCurrentUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuário não encontrado"));
        return UserDto.fromEntity(user);
    }

    public AuthResponse googleSignIn(GoogleSignInRequest request) {
        try {
            var payload = googleAuthService.verifyToken(request.getIdToken());
            User user = userRepository.findByEmail(payload.getEmail())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .email(payload.getEmail())
                            .name((String) payload.get("name"))
                            .authProvider(AuthProvider.GOOGLE)
                            .role(UserRole.PILOT)
                            .build()));

            return buildAuthResponse(user);
        } catch (Exception e) {
            throw new AuthException("Erro na autenticação Google: " + e.getMessage());
        }
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .token(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .user(UserDto.fromEntity(user))
                .build();
    }
}
