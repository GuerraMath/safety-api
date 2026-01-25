package io.github.guerramath.safety_api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Safety Checklist API",
				version = "1.0",
				description = "Gerenciamento de Risco Operacional - Cmte. Matheus Guerra"
		)
)
public class SafetyChecklistApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SafetyChecklistApiApplication.class, args);
	}
}