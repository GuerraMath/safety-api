package io.github.guerramath.safety_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// REMOVIDO: properties = { ... } (Isso causava duplicação de contexto)
@SpringBootTest
@ActiveProfiles("test") // Isso carrega o application-test.properties
public class SafetyChecklistApiApplicationTests {

	@Test
	void contextLoads() {
		// O contexto sobe usando as configurações do arquivo .properties
	}
}