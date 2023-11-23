package com.guardias.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Profesion;

@Repository
public interface ProfesionRepository extends JpaRepository<Profesion, Long> {

    Optional<Profesion> findById(int id);

    boolean existsById(int id);

    Optional<Profesion> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    // necesitamos para esAsistencial??
}