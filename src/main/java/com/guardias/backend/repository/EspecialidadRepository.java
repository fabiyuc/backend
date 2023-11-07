package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Integer> {

    Optional<Especialidad> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
