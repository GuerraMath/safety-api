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
    private String password;

    // CAMPOS QUE OS TESTES ESTÃO PEDINDO:
    private String passwordHash; // Alguns testes usam setPasswordHash
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;       // Requerido em AuthControllerTest e JwtServiceTest
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider authProvider; // Requerido em AuthServiceTest
    private boolean emailVerified;     // Requerido em JwtTestUtils
}