package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("JwtService Tests")
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
    }

    @Test
    @DisplayName("Deve gerar access token válido")
    void testGenerateAccessToken() {
        String token = jwtService.generateAccessToken(testUser);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token));
        assertFalse(jwtService.isRefreshToken(token));
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
    @DisplayName("Deve detectar refresh token")
    void testIsRefreshToken() {
        String refreshToken = jwtService.generateRefreshToken(testUser);
        String accessToken = jwtService.generateAccessToken(testUser);
        assertTrue(jwtService.isRefreshToken(refreshToken));
        assertFalse(jwtService.isRefreshToken(accessToken));
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

    @Test
    @DisplayName("Deve retornar false para token de outro usuario")
    void testExtractUserIdFromDifferentToken() {
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setName("Other User");
        otherUser.setEmail("other@example.com");
        otherUser.setRole(UserRole.PILOT);

        String token1 = jwtService.generateAccessToken(testUser);
        String token2 = jwtService.generateAccessToken(otherUser);

        String userId1 = jwtService.extractUserId(token1);
        String userId2 = jwtService.extractUserId(token2);

        assertEquals("1", userId1);
        assertEquals("2", userId2);
        assertNotEquals(userId1, userId2);
    }
}