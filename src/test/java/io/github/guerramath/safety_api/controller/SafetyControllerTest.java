package io.github.guerramath.safety_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.model.User;
import io.github.guerramath.safety_api.repository.UserRepository;
import io.github.guerramath.safety_api.service.SafetyService;
import io.github.guerramath.safety_api.util.JwtTestUtils;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class SafetyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private String authToken;

    @BeforeEach
    void setUp() {
        // Criar e salvar usuario de teste
        User testUser = JwtTestUtils.createTestPilot();
        testUser.setId(null); // Deixar JPA gerar o ID
        User savedUser = userRepository.save(testUser);

        // Gerar token JWT para o usuario
        authToken = JwtTestUtils.generateBearerToken(savedUser);
    }

    @Test
    @DisplayName("Deve criar um novo checklist via POST e retornar 200")
    void testCreateEvaluation() throws Exception {
        String requestBody = """
            {
                "pilotName": "Matheus Guerra",
                "healthScore": 4,
                "weatherScore": 4,
                "aircraftScore": 4,
                "missionScore": 4
            }
            """;

        mockMvc.perform(post("/api/v1/safety")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se os dados forem inválidos")
    void testInvalidRequest() throws Exception {
        // Testando um JSON vazio para disparar validações de @NotNull ou @NotBlank
        mockMvc.perform(post("/api/v1/safety")
                        .header("Authorization", authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}