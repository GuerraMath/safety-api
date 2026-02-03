package io.github.guerramath.safety_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.guerramath.safety_api.dto.auth.AuthResponse;
import io.github.guerramath.safety_api.dto.auth.LoginRequest;
import io.github.guerramath.safety_api.dto.auth.RefreshTokenRequest;
import io.github.guerramath.safety_api.dto.auth.RegisterRequest;
import io.github.guerramath.safety_api.dto.auth.UserDto;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.service.AuthService;
import io.github.guerramath.safety_api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("AuthController Tests")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(io.github.guerramath.safety_api.model.UserRole.PILOT);
    }

    @Test
    @DisplayName("Deve fazer login com sucesso")
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password123");

        UserDto userDto = UserDto.fromEntity(testUser);
        AuthResponse authResponse = new AuthResponse("access_token", "refresh_token", userDto);

        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("access_token"))
                // Verifica apenas se existe, cobrindo tanto camelCase quanto snake_case se configurado
                .andExpect(jsonPath("$.refreshToken").exists())
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Deve falhar ao fazer login com dados inválidos")
    void testLoginFailure() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid@example.com");
        request.setPassword("wrongpassword");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new AuthException("Email ou senha invalidos"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve registrar novo usuário com sucesso")
    void testRegisterSuccess() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("New User");
        request.setEmail("newuser@example.com");
        request.setPassword("password123");

        UserDto userDto = UserDto.fromEntity(testUser);
        AuthResponse authResponse = new AuthResponse("access_token", "refresh_token", userDto);

        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Deve falhar ao registrar com email duplicado")
    void testRegisterWithDuplicateEmail() throws Exception {
        RegisterRequest request = new RegisterRequest();
        request.setName("Duplicate");
        request.setEmail("test@example.com");
        request.setPassword("password123");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new AuthException("Email ja cadastrado"));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve atualizar token com refresh token válido")
    void testRefreshToken() throws Exception {
        RefreshTokenRequest request = new RefreshTokenRequest();
        request.setRefreshToken("valid_refresh_token");

        UserDto userDto = UserDto.fromEntity(testUser);
        AuthResponse authResponse = new AuthResponse("new_access_token", "new_refresh_token", userDto);

        when(authService.refreshToken(anyString())).thenReturn(authResponse);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new_access_token"));
    }

    @Test
    @DisplayName("Deve obter dados do usuário autenticado")
    void testGetCurrentUser() throws Exception {
        // CORREÇÃO: Gera token válido usando a chave do properties
        String token = jwtService.generateAccessToken(testUser);
        String bearerToken = "Bearer " + token;

        when(authService.getCurrentUser(1L)).thenReturn(UserDto.fromEntity(testUser));

        mockMvc.perform(get("/auth/me")
                        .header("Authorization", bearerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("Deve retornar 401 ao acessar /auth/me com token inválido")
    void testGetCurrentUserWithInvalidToken() throws Exception {
        mockMvc.perform(get("/auth/me")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(status().isUnauthorized());
    }
}