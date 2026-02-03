package io.github.guerramath.safety_api.util;

import io.github.guerramath.safety_api.model.AuthProvider;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.model.UserRole;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.SecretKey;

/**
 * Utilidades para gerar e manipular JWT tokens em testes.
 */
public class JwtTestUtils {

    private static final String TEST_SECRET =
            "test-secret-key-for-testing-purposes-only-256-bits";
    private static final long ACCESS_TOKEN_EXPIRATION = 86400000; // 24 horas
    private static final long REFRESH_TOKEN_EXPIRATION = 604800000; // 7 dias

    /**
     * Gera um access token JWT para um usuario de teste.
     */
    public static String generateAccessToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("role", user.getRole().name());
        claims.put("type", "access");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Gera um refresh token JWT para um usuario de teste.
     */
    public static String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("type", "refresh");

        return Jwts.builder()
                .claims(claims)
                .subject(user.getId().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Cria um usuario de teste com ID e papel especificados.
     */
    public static User createTestUser(Long id, String email, UserRole role) {
        User user = new User();
        user.setId(id);
        user.setName("Test User");
        user.setEmail(email);
        user.setPasswordHash("hashed_password");
        user.setRole(role);
        user.setAuthProvider(AuthProvider.LOCAL);
        user.setEmailVerified(true);
        return user;
    }

    /**
     * Cria um usuario de teste com configuracoes padrao.
     */
    public static User createTestPilot() {
        return createTestUser(1L, "test@example.com", UserRole.PILOT);
    }

    /**
     * Cria um usuario admin de teste.
     */
    public static User createTestAdmin() {
        return createTestUser(2L, "admin@example.com", UserRole.ADMIN);
    }

    /**
     * Gera um Bearer token completo (com prefixo "Bearer ").
     */
    public static String generateBearerToken(User user) {
        return "Bearer " + generateAccessToken(user);
    }

    private static SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(TEST_SECRET.getBytes());
    }
}
