package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Adicional;

@Repository
public interface AdicionalRepository extends JpaRepository<Adicional, Long> {

    Optional<List<Adicional>> findByActivoTrue();

    Optional<Adicional> findByNombre(String nombre);

    Optional<Adicional> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    // boolean existsByNombreAndIdNot(String nombre, Long id);

    List<Adicional> findByActivo(boolean activo);
}