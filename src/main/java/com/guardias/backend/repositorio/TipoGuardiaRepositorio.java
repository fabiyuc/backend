package com.guardias.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.TipoGuardia;

@Repository
public interface TipoGuardiaRepositorio extends JpaRepository<TipoGuardia, Long> {

    Optional<TipoGuardia> findByNombre(String nombre);
    boolean existsByNombre(String nombre);
    
    Optional<TipoGuardia> findByDescripcion(String descripcion);
    boolean existsByDescripcion(String descripcion);
}
