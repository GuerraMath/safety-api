package io.github.guerramath.safety_api.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO para requisicao de login via Google.
 */
public class GoogleSignInRequest {

    @NotBlank(message = "ID Token e obrigatorio")
    @JsonProperty("id_token")
    private String idToken;

    @NotBlank(message = "Email e obrigatorio")
    @Email(message = "Email invalido")
    private String email;

    private String name;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    public GoogleSignInRequest() {
    }

    public GoogleSignInRequest(String idToken, String email, String name, String avatarUrl) {
        this.idToken = idToken;
        this.email = email;
        this.name = name;
        this.avatarUrl = avatarUrl;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
