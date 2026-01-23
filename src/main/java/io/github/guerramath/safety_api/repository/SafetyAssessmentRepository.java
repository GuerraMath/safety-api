package io.github.guerramath.safety_api.repository;

import io.github.guerramath.safety_api.model.SafetyAssessment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyAssessmentRepository extends JpaRepository<SafetyAssessment, Long> {
    // O JpaRepository já nos dá métodos como save(), findAll() e delete()
}