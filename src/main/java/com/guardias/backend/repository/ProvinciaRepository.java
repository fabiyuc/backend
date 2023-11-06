package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Provincia;

@Repository
public interface ProvinciaRepository extends JpaRepository<Provincia, Integer> {

    Optional<Provincia> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

}
