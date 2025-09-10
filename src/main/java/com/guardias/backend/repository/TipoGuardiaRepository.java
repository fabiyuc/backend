package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoGuardia;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface TipoGuardiaRepository extends JpaRepository<TipoGuardia, Long> {

    Optional<List<TipoGuardia>> findByActivoTrue();

    Optional<TipoGuardia> findByDescripcion(String descripcion);

    Optional<TipoGuardia> findByNombre(TipoGuardiaEnum nombre);

    Optional<TipoGuardia> findById(Long id);

    boolean existsByNombre(TipoGuardiaEnum nombre);

    boolean existsById(Long id);

    boolean existsByDescripcion(String descripcion);

    List<TipoGuardia> findByActivo(boolean activo);

    @Query("SELECT DISTINCT tg FROM tiposGuardias tg JOIN tg.legajos l WHERE l.id = :legajoId AND tg.activo = true")
    List<TipoGuardia> findActiveByLegajoId(@Param("legajoId") Long legajoId);

}