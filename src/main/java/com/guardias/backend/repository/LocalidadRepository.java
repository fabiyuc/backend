package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Localidad;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    Optional<List<Localidad>> findByActivoTrue();

    Optional<Localidad> findByNombre(String nombre);

    Optional<Localidad> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Localidad> findByActivo(boolean activo);
}
