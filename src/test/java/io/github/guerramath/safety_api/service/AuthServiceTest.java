package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.dto.auth.AuthResponse;
import io.github.guerramath.safety_api.dto.auth.LoginRequest;
import io.github.guerramath.safety_api.dto.auth.RegisterRequest;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.AuthProvider;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private GoogleTokenVerifier googleTokenVerifier;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setPasswordHash("hashed_password");
        testUser.setAuthProvider(AuthProvider.LOCAL);
        testUser.setRole(io.github.guerramath.safety_api.model.UserRole.PILOT);
    }

    @Test
    @DisplayName("Deve fazer login com email e senha válidos")
    void testLoginSuccess() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("password123", "hashed_password"))
                .thenReturn(true);
        when(jwtService.generateAccessToken(testUser))
                .thenReturn("access_token");
        when(jwtService.generateRefreshToken(testUser))
                .thenReturn("refresh_token");

        // Act
        AuthResponse response = authService.login(request);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.getToken());
        assertEquals("refresh_token", response.getRefreshToken());
        assertNotNull(response.getUser());
        assertEquals("test@example.com", response.getUser().getEmail());
    }

    @Test
    @DisplayName("Deve lançar exceção com email inválido")
    void testLoginWithInvalidEmail() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("invalid@example.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção com senha inválida")
    void testLoginWithInvalidPassword() {
        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("wrongpassword");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches("wrongpassword", "hashed_password"))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar login com conta Google")
    void testLoginWithGoogleAccount() {
        // Arrange
        testUser.setAuthProvider(AuthProvider.GOOGLE);
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(testUser));

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.login(request);
        });
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void testRegisterSuccess() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("newuser@example.com"))
                .thenReturn(false);
        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded_password");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> {
                    User user = invocation.getArgument(0);
                    user.setId(2L);
                    return user;
                });
        when(jwtService.generateAccessToken(any(User.class)))
                .thenReturn("access_token");
        when(jwtService.generateRefreshToken(any(User.class)))
                .thenReturn("refresh_token");

        // Act
        AuthResponse response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.getToken());
        assertEquals("refresh_token", response.getRefreshToken());
        assertEquals("newuser@example.com", response.getUser().getEmail());

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar com email duplicado")
    void testRegisterWithDuplicateEmail() {
        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(userRepository.existsByEmail("test@example.com"))
                .thenReturn(true);

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.register(request);
        });
    }

    @Test
    @DisplayName("Deve atualizar token com refresh token válido")
    void testRefreshTokenSuccess() {
        // Arrange
        String refreshToken = "valid_refresh_token";

        when(jwtService.isTokenValid(refreshToken))
                .thenReturn(true);
        when(jwtService.isRefreshToken(refreshToken))
                .thenReturn(true);
        when(jwtService.extractUserId(refreshToken))
                .thenReturn("1");
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(testUser));
        when(jwtService.generateAccessToken(testUser))
                .thenReturn("new_access_token");
        when(jwtService.generateRefreshToken(testUser))
                .thenReturn("new_refresh_token");

        // Act
        AuthResponse response = authService.refreshToken(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("new_access_token", response.getToken());
    }

    @Test
    @DisplayName("Deve lançar exceção com refresh token expirado")
    void testRefreshTokenExpired() {
        // Arrange
        String refreshToken = "expired_refresh_token";

        when(jwtService.isTokenValid(refreshToken))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.refreshToken(refreshToken);
        });
    }

    @Test
    @DisplayName("Deve lançar exceção se token não é um refresh token")
    void testRefreshTokenInvalid() {
        // Arrange
        String token = "access_token";

        when(jwtService.isTokenValid(token))
                .thenReturn(true);
        when(jwtService.isRefreshToken(token))
                .thenReturn(false);

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.refreshToken(token);
        });
    }

    @Test
    @DisplayName("Deve obter usuário autenticado pelo ID")
    void testGetCurrentUser() {
        // Arrange
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(testUser));

        // Act
        var userDto = authService.getCurrentUser(1L);

        // Assert
        assertNotNull(userDto);
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("Test User", userDto.getName());
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário inexistente")
    void testGetCurrentUserNotFound() {
        // Arrange
        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AuthException.class, () -> {
            authService.getCurrentUser(999L);
        });
    }
}
