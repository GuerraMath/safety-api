package io.github.guerramath.safety_api.repository;
import io.github.guerramath.safety_api.model.SafetyEvaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SafetyRepository extends JpaRepository<SafetyEvaluation, Long> { }