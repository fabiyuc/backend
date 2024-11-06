package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.Permisos;

public interface PermisosRepository extends JpaRepository<Permisos, Long> {

    Optional<List<Permisos>> findByActivoTrue();

    boolean existsById(Long id);

    boolean existsByIdAsistencial(Long idAsistencial);

    Optional<Permisos> findByIdAsistencial(Long idAsistencial);

    Optional<Permisos> findByIdAsistencialAndActivoTrue(@Param("idAsistencial") Long idAsistencial);

}
