package io.github.guerramath.safety_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // <--- A mágica acontece aqui
class SafetyChecklistApiApplicationTests {

	@Test
	void contextLoads() {
		// Se o contexto subir usando o H2, o teste passa!
	}

}