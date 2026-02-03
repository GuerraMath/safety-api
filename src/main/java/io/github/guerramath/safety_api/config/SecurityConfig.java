package io.github.guerramath.safety_api.config;

import io.github.guerramath.safety_api.repository.UserRepository;
import io.github.guerramath.safety_api.security.JwtAuthenticationFilter;
import io.github.guerramath.safety_api.service.JwtService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuracao de seguranca da aplicacao.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura a cadeia de filtros de seguranca.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService,
            UserRepository userRepository) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Endpoints publicos (autenticacao)
                .requestMatchers("/auth/**").permitAll()
                // Endpoints de status e documentacao
                .requestMatchers("/api/v1/status").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html").permitAll()
                // Endpoints de safety requerem autenticacao
                .requestMatchers("/api/v1/safety/**").authenticated()
                .requestMatchers("/api/evaluations/**").authenticated()
                // Demais endpoints requerem autenticacao
                .anyRequest().authenticated()
            )
            .addFilterBefore(
                new JwtAuthenticationFilter(jwtService, userRepository),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Bean para encoder de senhas.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
