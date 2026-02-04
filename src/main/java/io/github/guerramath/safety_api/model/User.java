package io.github.guerramath.safety_api.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String googleId;
    
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    private AuthProvider authProvider;

    private boolean emailVerified;

    // Métodos de conveniência para compatibilidade com testes antigos
    public String getPasswordHash() {
        return this.password;
    }

    public void setPasswordHash(String passwordHash) {
        this.password = passwordHash;
    }
}
