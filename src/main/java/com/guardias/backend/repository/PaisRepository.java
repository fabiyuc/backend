package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    Optional<List<Pais>> findByActivoTrue();

    Optional<Pais> findByNombre(String nombre);

    Optional<Pais> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsByNacionalidad(String nacionalidad);

    boolean existsById(Long id);

    List<Pais> findByActivo(boolean activo);

}