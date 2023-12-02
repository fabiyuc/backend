package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.Asistencial;


@Repository
public interface AsistencialRepository extends JpaRepository<Asistencial, Long>{
    
    Optional<Asistencial> findByDni(int dni);
    
    boolean existsByDni(int dni);
}
