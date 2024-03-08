package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.DistribucionGira;

@Repository
public interface DistribucionGiraRepository extends JpaRepository<DistribucionGira, Long> {

    Optional<DistribucionGira> findById(Long id);

    List<DistribucionGira> findByFechaInicio(LocalDate fechaInicio);

    @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.persona.id = :personaId")
    Optional<List<DistribucionGira>> findByPersonaId(@Param("personaId") Long personaId);

    @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.efector.id = :efectorId")
    Optional<List<DistribucionGira>> findByEfectorId(@Param("efectorId") Long efectorId);

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    boolean existsByPersonaId(Long personaId);

    List<DistribucionGira> findByActivoTrue();
}
