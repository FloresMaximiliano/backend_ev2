package org.example.ev2_backend.repository;

import org.example.ev2_backend.model.Lote;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoteRepository extends JpaRepository<Lote, Long> {
    Optional<Lote> findByCodeIgnoreCase(String code);
}