package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Suspencion;

@Repository
public interface SuspencionRepository extends JpaRepository<Suspencion,Long> {

    Optional<Suspencion> findById(Long id); 
    boolean existsById(Long id);

    Boolean existsByFechaInicio(LocalDate fechaInicio);
    Boolean existsByFechaFin(LocalDate fechaFin);

    Optional<Suspencion> findByFechaInicio(LocalDate fechaInicio);
    Optional<Suspencion> findByFechaFin(LocalDate fechaFin);

    

}
