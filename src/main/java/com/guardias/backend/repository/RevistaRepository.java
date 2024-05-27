package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Revista;
import com.guardias.backend.enums.AgrupacionEnum;

@Repository
public interface RevistaRepository extends JpaRepository<Revista, Long> {

    Optional<List<Revista>> findByActivoTrue();

    Optional<Revista> findById(Long id);

    Optional<Revista> findByAgrupacion(AgrupacionEnum agrupacion);

    boolean existsById(Long id);

    boolean existsByAgrupacion(AgrupacionEnum agrupacion);

    List<Revista> findByActivo(boolean activo);
}
