package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.dto.auth.*;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final GoogleTokenVerifier googleTokenVerifier;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Email ou senha invalidos"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Email ou senha invalidos");
        }

        return buildAuthResponse(user);
    }

    public AuthResponse googleSignIn(GoogleSignInRequest request) {
        try {
            var payload = googleTokenVerifier.verify(request.getIdToken());
            if (payload == null) {
                throw new AuthException("Token do Google invalido");
            }

            User user = userRepository.findByEmail(payload.getEmail())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .email(payload.getEmail())
                            .name((String) payload.get("name"))
                            .authProvider(io.github.guerramath.safety_api.model.AuthProvider.GOOGLE)
                            .role(io.github.guerramath.safety_api.model.UserRole.PILOT)
                            .emailVerified(true)
                            .build()));

            return buildAuthResponse(user);
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("Erro na autenticacao Google: " + e.getMessage());
        }
    }

    public AuthResponse register(RegisterRequest request) {
        User existingUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if (existingUser != null) {
            throw new AuthException("Email ja cadastrado");
        }

        User newUser = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .authProvider(io.github.guerramath.safety_api.model.AuthProvider.LOCAL)
                .role(io.github.guerramath.safety_api.model.UserRole.PILOT)
                .emailVerified(true)
                .build();

        userRepository.save(newUser);
        return buildAuthResponse(newUser);
    }

    public AuthResponse refreshToken(String refreshTokenValue) {
        try {
            String userId = jwtService.extractUserId(refreshTokenValue);
            if (!jwtService.isRefreshToken(refreshTokenValue)) {
                throw new AuthException("Token invalido");
            }

            User user = userRepository.findById(Long.parseLong(userId))
                    .orElseThrow(() -> new AuthException("Usuario nao encontrado"));

            return buildAuthResponse(user);
        } catch (AuthException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthException("Erro ao atualizar token: " + e.getMessage());
        }
    }

    public UserDto getCurrentUser(long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuario nao encontrado"));
        return UserDto.fromEntity(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        return AuthResponse.builder()
                .token(jwtService.generateAccessToken(user))
                .refreshToken(jwtService.generateRefreshToken(user))
                .user(UserDto.fromEntity(user))
                .build();
    }
}