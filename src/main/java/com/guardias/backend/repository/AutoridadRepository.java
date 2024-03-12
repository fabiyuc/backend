package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Autoridad;

@Repository
public interface AutoridadRepository extends JpaRepository<Autoridad, Long> {

    // Optional<Autoridad> findById(Long id);
    Optional<Autoridad> findByNombre(String nombre);

    // boolean existsById(Long id);

    boolean existsByNombre(String nombre);

    List<Autoridad> findByActivoTrue();
}
