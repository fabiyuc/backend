package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoGuardia;

@Repository
public interface TipoGuardiaRepository extends JpaRepository<TipoGuardia, Long> {

    Optional<List<TipoGuardia>> findByActivoTrue();

    Optional<TipoGuardia> findByDescripcion(String descripcion);

    Optional<TipoGuardia> findByNombre(String nombre);

    Optional<TipoGuardia> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    boolean existsByDescripcion(String descripcion);

    List<TipoGuardia> findByActivo(boolean activo);

}