package io.github.guerramath.safety_api.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * DTO para resposta de autenticação.
 */
public class AuthResponse {

    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;

    private UserDto user;
}