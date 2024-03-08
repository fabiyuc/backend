package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.DistribucionGuardia;

@Repository
public interface DistribucionGuardiaRepository extends JpaRepository<DistribucionGuardia, Long> {

    Optional<DistribucionGuardia> findById(Long id);

    List<DistribucionGuardia> findByFechaInicio(LocalDate fechaInicio);

    @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.persona.id = :personaId")
    Optional<List<DistribucionGuardia>> findByPersonaId(@Param("personaId") Long personaId);

    @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.efector.id = :efectorId")
    Optional<List<DistribucionGuardia>> findByEfectorId(@Param("efectorId") Long efectorId);

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    boolean existsByPersonaId(Long personaId);

    List<DistribucionGuardia> findByActivoTrue();
}
