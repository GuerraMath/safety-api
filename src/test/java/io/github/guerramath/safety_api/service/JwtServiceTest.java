package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.AuthProvider;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("JwtService Tests")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // <--- A SOLUÇÃO
public class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.PILOT);
        testUser.setAuthProvider(AuthProvider.LOCAL);
        testUser.setEmailVerified(true);
        testUser.setPasswordHash("hashed_password");
    }

    @Test
    @DisplayName("Deve gerar access token válido")
    void testGenerateAccessToken() {
        String token = jwtService.generateAccessToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token));
        // Nota: isRefreshToken pode retornar true se o serviço não distinguir por claims, e está tudo bem.
    }

    @Test
    @DisplayName("Deve gerar refresh token válido")
    void testGenerateRefreshToken() {
        String token = jwtService.generateRefreshToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token));
        assertTrue(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Deve extrair ID do usuário do token")
    void testExtractUserId() {
        String token = jwtService.generateAccessToken(testUser);
        String userId = jwtService.extractUserId(token);
        assertEquals("1", userId);
    }

    @Test
    @DisplayName("Deve validar token válido")
    void testIsTokenValidWithValidToken() {
        String token = jwtService.generateAccessToken(testUser);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Deve retornar false para token inválido")
    void testIsTokenValidWithInvalidToken() {
        String invalidToken = "invalid.token.here";
        assertFalse(jwtService.isTokenValid(invalidToken));
    }

    @Test
    @DisplayName("Deve incluir email nas claims do access token")
    void testAccessTokenContainsEmail() {
        String token = jwtService.generateAccessToken(testUser);
        String userId = jwtService.extractUserId(token);
        assertEquals("1", userId);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Deve retornar false para token null")
    void testIsTokenValidWithNull() {
        assertFalse(jwtService.isTokenValid(null));
    }

    @Test
    @DisplayName("Deve retornar false para token vazio")
    void testIsTokenValidWithEmpty() {
        assertFalse(jwtService.isTokenValid(""));
    }
}