package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.RegistroActividad;

@Repository
public interface RegistroActividadRepository extends JpaRepository<RegistroActividad, Long> {

    Optional<RegistroActividad> findById(Long id);

    boolean existsById(Long id);

    List<RegistroActividad> findByActivoTrue();
}
