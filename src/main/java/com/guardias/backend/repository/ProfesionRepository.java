package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.Profesion;

public interface ProfesionRepository extends JpaRepository<Profesion, Integer> {
    
    Optional<Profesion> findById(int id);
    boolean existsById(int id);

    Optional<Profesion> findByNombre(String nombre);
    boolean existsByNombre(String nombre);

    //necesitamos para esAsistencial??
}
