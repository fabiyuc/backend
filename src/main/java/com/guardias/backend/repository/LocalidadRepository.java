package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Localidad;

@Repository
public interface LocalidadRepository extends JpaRepository<Localidad, Long> {

    Optional<Localidad> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
