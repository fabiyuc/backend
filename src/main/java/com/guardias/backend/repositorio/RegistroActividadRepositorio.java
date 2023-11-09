package com.guardias.backend.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.RegistroActividad;

@Repository
public interface RegistroActividadRepositorio extends JpaRepository<RegistroActividad,Long>{

    /* Optional<RegistroActividad> findById(int id); */
    boolean existsById(int id);
    
}
