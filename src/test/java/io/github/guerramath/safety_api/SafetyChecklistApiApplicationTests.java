package io.github.guerramath.safety_api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.datasource.driver-class-name=org.h2.Driver"
})
@ActiveProfiles("test")
class SafetyChecklistApiApplicationTests {
	@Test
	void contextLoads() {
		// Agora o Spring e o Hibernate sao forcados a usar o H2
	}
}