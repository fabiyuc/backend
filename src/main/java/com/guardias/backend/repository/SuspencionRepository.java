package com.guardias.backend.repository;

import java.sql.Date;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.Suspencion;

public interface SuspencionRepository extends JpaRepository<Suspencion,Integer> {

    Optional<Suspencion> findById(int id); 
    boolean existsById(int id);

    Optional<Suspencion> findByFechaInicio(Date fechaInicio); 
    boolean existsByFechaInicio(Date fechaInicio);
    
    Optional<Suspencion> findByFechaFin(Date fechaFin); 
    boolean existsByFechaFin(Date fechaFin);

}
