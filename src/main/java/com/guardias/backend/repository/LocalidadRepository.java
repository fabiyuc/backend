package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guardias.backend.entity.Localidad;

public interface LocalidadRepository extends JpaRepository<Localidad, Integer> {

    Optional<Localidad> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
