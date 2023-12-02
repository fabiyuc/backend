package com.guardias.backend.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.NoAsistencial;

@Repository
public interface NoAsistencialRepository extends JpaRepository<NoAsistencial, Long> {
    
    Optional<NoAsistencial> findByDni(int dni);
    
    boolean existsByDni(int dni);
}
