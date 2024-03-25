package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.TipoRevista;

@Repository
public interface TipoRevistaRepository extends JpaRepository<TipoRevista, Long> {
    Optional<TipoRevista> findByNombre(String nombre);

    Optional<TipoRevista> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<TipoRevista> findByActivoTrue();
}

/*
 * Optional<TipoRevista> findByNombre(String nombre);
 * 
 * boolean existsByNombre(String nombre);
 * 
 * List<TipoRevista> findByActivoTrue();
 * }
 */