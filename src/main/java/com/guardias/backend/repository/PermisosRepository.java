package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.Permisos;

public interface PermisosRepository extends JpaRepository<Permisos, Long> {

    Optional<List<Permisos>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByPersonaId(Long personaId);

    Optional<Permisos> findByPersonaId(Long personaId);

    Optional<Permisos> findByPersonaIdAndActivoTrue(Long personaId);

    @Query("SELECT p FROM permisos p JOIN p.efectores e WHERE TYPE(p.persona) = asistenciales AND e.id = :idEfector")
    List<Permisos> findPermisosByEfectorAndAsistencial(@Param("idEfector") Long idEfector);


}
