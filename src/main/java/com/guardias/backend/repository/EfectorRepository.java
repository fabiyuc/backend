package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Efector;

@Repository
public interface EfectorRepository extends JpaRepository<Efector, Long> {
    Optional<Efector> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
