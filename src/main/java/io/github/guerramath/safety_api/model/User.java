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
    private UserRole role;       // Requerido em AuthControllerTest e JwtServiceTest
    private AuthProvider authProvider; // Requerido em AuthServiceTest
    private boolean emailVerified;     // Requerido em JwtTestUtils
}