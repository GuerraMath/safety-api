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
@DisplayName("JwtService Tests")
@ActiveProfiles("test")
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
    void testGenerateAccessToken() {
        String token = jwtService.generateAccessToken(testUser);
        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void testIsTokenValidWithNull() {
        assertFalse(jwtService.isTokenValid(null));
    }

    @Test
    void testIsTokenValidWithEmpty() {
        assertFalse(jwtService.isTokenValid(""));
    }

    // ... (Os outros métodos de teste que já tínhamos) ...
}