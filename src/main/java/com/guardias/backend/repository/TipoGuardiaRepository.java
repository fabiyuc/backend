package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.TipoGuardia;

@Repository
public interface TipoGuardiaRepository extends JpaRepository<TipoGuardia, Long> {

    Optional<TipoGuardia> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    Optional<TipoGuardia> findByDescripcion(String descripcion);

    boolean existsByDescripcion(String descripcion);

    List <TipoGuardia> findByActivoTrue();
}
