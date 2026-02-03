package io.github.guerramath.safety_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.guerramath.safety_api.model.SafetyEvaluation;
import io.github.guerramath.safety_api.service.SafetyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class SafetyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request se os dados forem inválidos")
    void testInvalidRequest() throws Exception {
        // Testando um JSON vazio para disparar validações de @NotNull ou @NotBlank
        mockMvc.perform(post("/api/v1/safety")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}