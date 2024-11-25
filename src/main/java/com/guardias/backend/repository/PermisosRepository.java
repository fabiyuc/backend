package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.Permisos;

public interface PermisosRepository extends JpaRepository<Permisos, Long> {

    Optional<List<Permisos>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByPersonaId(Long personaId);

    Optional<Permisos> findByPersonaId(Long personaId);

    Optional<Permisos> findByPersonaIdAndActivoTrue(Long personaId);

}
