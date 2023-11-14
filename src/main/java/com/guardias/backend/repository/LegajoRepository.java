package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.guardias.backend.entity.Legajo;

public interface LegajoRepository extends JpaRepository<Legajo,Long>{
    
    Optional<Legajo> findById(Long id);
    boolean existsById(Long id);
}
