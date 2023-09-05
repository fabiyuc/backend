package com.guardias.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.Profesional;


@Repository
public interface ProfesionalRepositorio extends JpaRepository<Profesional, Long>{

    Optional<Profesional> findByDni(int dni);
    boolean existsByDni(int dni);
}
