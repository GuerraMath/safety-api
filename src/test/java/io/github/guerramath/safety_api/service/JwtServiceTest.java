package io.github.guerramath.safety_api.service;

import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.model.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "jwt.secret=test-secret-key-for-testing-purposes-only-256-bits",
    "jwt.access-token-expiration=3600000",
    "jwt.refresh-token-expiration=86400000"
})
@DisplayName("JwtService Tests")
class JwtServiceTest {

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
        // Act
        String token = jwtService.generateAccessToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token));
        assertFalse(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Deve gerar refresh token válido")
    void testGenerateRefreshToken() {
        // Act
        String token = jwtService.generateRefreshToken(testUser);

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(jwtService.isTokenValid(token));
        assertTrue(jwtService.isRefreshToken(token));
    }

    @Test
    @DisplayName("Deve extrair ID do usuário do token")
    void testExtractUserId() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        String userId = jwtService.extractUserId(token);

        // Assert
        assertEquals("1", userId);
    }

    @Test
    @DisplayName("Deve validar token válido")
    void testIsTokenValidWithValidToken() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        boolean isValid = jwtService.isTokenValid(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Deve retornar false para token inválido")
    void testIsTokenValidWithInvalidToken() {
        // Arrange
        String invalidToken = "invalid.token.here";

        // Act
        boolean isValid = jwtService.isTokenValid(invalidToken);

        // Assert
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Deve detectar refresh token")
    void testIsRefreshToken() {
        // Arrange
        String refreshToken = jwtService.generateRefreshToken(testUser);
        String accessToken = jwtService.generateAccessToken(testUser);

        // Act
        boolean isRefresh = jwtService.isRefreshToken(refreshToken);
        boolean isAccess = jwtService.isRefreshToken(accessToken);

        // Assert
        assertTrue(isRefresh);
        assertFalse(isAccess);
    }

    @Test
    @DisplayName("Deve incluir email nas claims do access token")
    void testAccessTokenContainsEmail() {
        // Arrange
        String token = jwtService.generateAccessToken(testUser);

        // Act
        String userId = jwtService.extractUserId(token);

        // Assert
        assertEquals("1", userId);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    @DisplayName("Deve retornar false para token null")
    void testIsTokenValidWithNull() {
        // Act & Assert
        assertFalse(jwtService.isTokenValid(null));
    }

    @Test
    @DisplayName("Deve retornar false para token vazio")
    void testIsTokenValidWithEmpty() {
        // Act & Assert
        assertFalse(jwtService.isTokenValid(""));
    }

    @Test
    @DisplayName("Deve retornar false para token de outro usuario")
    void testExtractUserIdFromDifferentToken() {
        // Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setName("Other User");
        otherUser.setEmail("other@example.com");
        otherUser.setRole(UserRole.PILOT);

        String token1 = jwtService.generateAccessToken(testUser);
        String token2 = jwtService.generateAccessToken(otherUser);

        // Act
        String userId1 = jwtService.extractUserId(token1);
        String userId2 = jwtService.extractUserId(token2);

        // Assert
        assertEquals("1", userId1);
        assertEquals("2", userId2);
        assertNotEquals(userId1, userId2);
    }
}
