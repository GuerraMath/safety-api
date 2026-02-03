package io.github.guerramath.safety_api.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import io.github.guerramath.safety_api.dto.auth.AuthResponse;
import io.github.guerramath.safety_api.dto.auth.GoogleSignInRequest;
import io.github.guerramath.safety_api.dto.auth.LoginRequest;
import io.github.guerramath.safety_api.dto.auth.RegisterRequest;
import io.github.guerramath.safety_api.dto.auth.UserDto;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.AuthProvider;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.repository.UserRepository;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Servico de autenticacao.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final GoogleTokenVerifier googleTokenVerifier;

    /**
     * Construtor do AuthService.
     */
    public AuthService(UserRepository userRepository,
                       JwtService jwtService,
                       PasswordEncoder passwordEncoder,
                       GoogleTokenVerifier googleTokenVerifier) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.googleTokenVerifier = googleTokenVerifier;
    }

    /**
     * Realiza login com email e senha.
     */
    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException("Email ou senha invalidos"));

        if (user.getAuthProvider() == AuthProvider.GOOGLE) {
            throw new AuthException("Esta conta usa login com Google. Use o botao 'Continuar com Google'.");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new AuthException("Email ou senha invalidos");
        }

        return createAuthResponse(user);
    }

    /**
     * Registra um novo usuario.
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email ja cadastrado");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setAuthProvider(AuthProvider.LOCAL);

        user = userRepository.save(user);

        return createAuthResponse(user);
    }

    /**
     * Realiza login/registro via Google.
     */
    public AuthResponse googleSignIn(GoogleSignInRequest request) {
        // Verifica o ID Token do Google
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getIdToken());

        if (payload == null) {
            throw new AuthException("Token do Google invalido ou expirado");
        }

        // Extrai informacoes do payload
        String googleId = payload.getSubject();
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        boolean emailVerified = Boolean.TRUE.equals(payload.getEmailVerified());

        // Usa os dados do request como fallback
        if (name == null || name.isBlank()) {
            name = request.getName();
        }
        if (pictureUrl == null || pictureUrl.isBlank()) {
            pictureUrl = request.getAvatarUrl();
        }

        // Busca ou cria o usuario
        Optional<User> existingUser = userRepository.findByEmail(email);

        User user;
        if (existingUser.isPresent()) {
            user = existingUser.get();

            // Atualiza informacoes do Google se necessario
            if (user.getGoogleId() == null) {
                user.setGoogleId(googleId);
                user.setAuthProvider(AuthProvider.GOOGLE);
            }

            // Atualiza avatar se mudou
            if (pictureUrl != null && !pictureUrl.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(pictureUrl);
            }

            // Marca email como verificado
            if (emailVerified && !user.isEmailVerified()) {
                user.setEmailVerified(true);
            }

            user = userRepository.save(user);
        } else {
            // Cria novo usuario
            user = new User();
            user.setName(name != null ? name : "Usuario");
            user.setEmail(email);
            user.setGoogleId(googleId);
            user.setAvatarUrl(pictureUrl);
            user.setAuthProvider(AuthProvider.GOOGLE);
            user.setEmailVerified(emailVerified);

            user = userRepository.save(user);
        }

        return createAuthResponse(user);
    }

    /**
     * Atualiza o token usando refresh token.
     */
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isTokenValid(refreshToken)) {
            throw new AuthException("Refresh token invalido ou expirado");
        }

        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new AuthException("Token fornecido nao e um refresh token");
        }

        String userId = jwtService.extractUserId(refreshToken);
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new AuthException("Usuario nao encontrado"));

        return createAuthResponse(user);
    }

    /**
     * Busca o usuario pelo ID.
     */
    public UserDto getCurrentUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException("Usuario nao encontrado"));

        return UserDto.fromEntity(user);
    }

    /**
     * Cria a resposta de autenticacao com tokens.
     */
    private AuthResponse createAuthResponse(User user) {
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        UserDto userDto = UserDto.fromEntity(user);

        return new AuthResponse(accessToken, refreshToken, userDto);
    }
}
