package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Legajo;

@Repository
public interface LegajoRepository extends JpaRepository<Legajo, Long> {

    List<Legajo> findByActivoTrue();

    Optional<Legajo> findById(Long id);

    boolean existsById(Long id);

    boolean existsByMatriculaProvincialAndActivoTrue(String matriculaProvincial);

    Optional<Legajo> findByMatriculaProvincialAndActivoTrue(String matriculaProvincial);

    Optional<Legajo> findByPersonaIdAndActivoTrue(Long personaId);

    List<Legajo> findByActivo(boolean activo);

    boolean existsByPersonaIdAndActivoTrue(Long personaId);

    @Query("SELECT l FROM legajos l " +
            "WHERE l.persona.id = :id " +
            "AND l.activo = true " +
            "AND l.esAutoridad = false")
    List<Legajo> findLegajosByPersonaId(@Param("id") Long id);

    @Query("SELECT l FROM legajos l " +
            "WHERE l.persona.id = :id " +
            "AND l.activo = true " +
            "AND l.esAutoridad = true")
    Optional<Legajo> findLegajoAutoridadByPersonaId(@Param("id") Long id);
}