package com.guardias.backend.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.guardias.backend.modelo.RegistroActividad;

@Repository
public interface RegistroActividadRepositorio extends JpaRepository<RegistroActividad,Integer>{

    Optional<RegistroActividad> findById(int id);
    boolean existsById(int id);
    
}
