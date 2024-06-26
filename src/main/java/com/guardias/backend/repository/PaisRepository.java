package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Pais;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long> {

    Optional<Pais> findByNombre(String nombre);

    public Optional<Pais> findByNacionalidad(String nacionalidad);

    boolean existsByNombre(String nombre);

    boolean existsByNacionalidad(String nacionalidad);

    List<Pais> findByActivoTrue();

}