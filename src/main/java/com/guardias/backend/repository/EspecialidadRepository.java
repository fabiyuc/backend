package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Especialidad;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    Optional<Especialidad> findByNombre(String nombre);

    Optional<List<Especialidad>> findByActivoTrue();

    Optional<Especialidad> findById(Long id);

    boolean existsByNombre(String nombre);

    boolean existsById(Long id);

    List<Especialidad> findByActivo(boolean activo);

    List<Especialidad> findByProfesionId(Long profesionId);
}
