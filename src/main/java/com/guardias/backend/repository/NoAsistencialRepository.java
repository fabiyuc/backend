package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.NoAsistencial;
import com.guardias.backend.modelo.Profesional;

@Repository
public interface NoAsistencialRepository extends JpaRepository<NoAsistencial, Long> {
    
     Optional<Profesional> findByDni(int dni);
    boolean existsByDni(int dni);
}
