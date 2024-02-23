package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Profesion;

@Repository
public interface ProfesionRepository extends JpaRepository<Profesion, Long> {
    
    Optional<Profesion> findById(Long id);
    
    boolean existsById(Long id);

    Optional<Profesion> findByNombre(String nombre);

    boolean existsByNombre(String nombre);

    List<Profesion> findByAsistencialTrue();
    
    List<Profesion> findByAsistencialFalse();

    
}
