package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.Efector;

public interface EfectorRepository extends JpaRepository<Efector, Long> {
    Optional<Efector> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
