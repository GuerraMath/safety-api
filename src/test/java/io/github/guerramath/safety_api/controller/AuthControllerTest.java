package io.github.guerramath.safety_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.guerramath.safety_api.dto.auth.*;
import io.github.guerramath.safety_api.exception.AuthException;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.service.AuthService;
import io.github.guerramath.safety_api.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DisplayName("AuthController Tests")
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private io.github.guerramath.safety_api.repository.UserRepository userRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        testUser = new User();
        testUser.setName("Test User");
        testUser.setEmail("test@example.com");
        testUser.setRole(io.github.guerramath.safety_api.model.UserRole.PILOT);
        testUser = userRepository.save(testUser);
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

        User newUser = new User();
        newUser.setId(2L);
        newUser.setName("New User");
        newUser.setEmail("newuser@example.com");
        newUser.setRole(io.github.guerramath.safety_api.model.UserRole.PILOT);

        UserDto userDto = UserDto.fromEntity(newUser);
        AuthResponse authResponse = new AuthResponse("access_token", "refresh_token", userDto);

        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("newuser@example.com"));
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

        when(authService.refreshToken(anyString()))
                .thenReturn(authResponse);

        mockMvc.perform(post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("new_access_token"));
    }

    @Test
    @DisplayName("Deve obter dados do usuário autenticado")
    void testGetCurrentUser() throws Exception {
        String token = jwtService.generateAccessToken(testUser);
        String bearerToken = "Bearer " + token;

        when(authService.getCurrentUser(anyLong()))
                .thenReturn(UserDto.fromEntity(testUser));

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

    @Test
    @DisplayName("Deve fazer logout com sucesso")
    void testLogout() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve validar email em requisição de login")
    void testLoginWithInvalidEmail() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setEmail("invalid-email");
        request.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Deve validar campos obrigatórios em register")
    void testRegisterWithMissingFields() throws Exception {
        String invalidRequest = "{ \"name\": \"Test\" }";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
