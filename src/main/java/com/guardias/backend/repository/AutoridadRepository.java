package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Autoridad;

@Repository
public interface AutoridadRepository extends JpaRepository<Autoridad, Long> {

  Optional<List<Autoridad>> findByActivoTrue();

  Optional<Autoridad> findById(Long id);

  @Query("SELECT a FROM autoridades a WHERE a.persona.id = :personaId")
  Optional<List<Autoridad>> findByPersonaId(@Param("personaId") Long personaId);

  boolean existsById(Long id);

  boolean existsByPersonaId(Long personaId);

  List<Autoridad> findByActivo(boolean activo);

  boolean existsByPersonaIdAndActivoTrueAndConfirmadoTrue(Long idPersona);

  boolean existsByPersonaIdAndActivoTrue(Long idPersona);

  @Query("""
      SELECT DISTINCT a
      FROM autoridades a
      JOIN a.persona p
      JOIN p.legajos l
      WHERE a.activo = true
        AND l.activo = true
        AND l.esAutoridad = true
        AND p.id = :idPersona
      """)
  Optional<Autoridad> findActiveAutoridadLegajoByPersonaId(@Param("idPersona") Long idPersona);

  @Query("SELECT COUNT(a) FROM autoridades a " +
      "WHERE a.confirmado is NULL " +
      "AND a.activo = true")
  Long countPendientes();

}
