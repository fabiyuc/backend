package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Especialidad;

@Repository
public interface EspecialidadRepository extends JpaRepository<Especialidad, Long> {

    Optional<Especialidad> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Especialidad> findByActivoTrue();
}
