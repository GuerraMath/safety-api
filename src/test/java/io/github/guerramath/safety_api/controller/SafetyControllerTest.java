package io.github.guerramath.safety_api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2) // <--- A SOLUÇÃO
public class SafetyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Deve criar um novo checklist via POST e retornar 200")
    @WithMockUser(username = "piloto@teste.com", authorities = {"PILOT"})
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se os dados forem inválidos")
    @WithMockUser(username = "piloto@teste.com", authorities = {"PILOT"})
    void testInvalidRequest() throws Exception {
        mockMvc.perform(post("/api/v1/safety")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}