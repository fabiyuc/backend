package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Autoridad;

@Repository
public interface AutoridadRepository extends JpaRepository<Autoridad,Long> {
    
    Optional<Autoridad> findById(Long id);
    boolean existsById(Long id);
}
