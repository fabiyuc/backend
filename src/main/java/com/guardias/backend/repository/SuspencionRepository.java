package com.guardias.backend.repository;

import java.sql.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.Suspencion;

public interface SuspencionRepository extends JpaRepository<Suspencion,Long> {

    Optional<Suspencion> findById(Long id); 
    boolean existsById(Long id);

    Optional<Suspencion> findByFechaInicio(Date fechaInicio); 
    boolean existsByFechaInicio(Date fechaInicio);
    
    Optional<Suspencion> findByFechaFin(Date fechaFin); 
    boolean existsByFechaFin(Date fechaFin);

}
