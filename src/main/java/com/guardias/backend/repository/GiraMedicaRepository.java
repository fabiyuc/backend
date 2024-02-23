package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.guardias.backend.entity.GiraMedica;

@Repository
public interface GiraMedicaRepository extends JpaRepository<GiraMedica, Long> {
    
    Optional<List<GiraMedica>> findByFecha(LocalDate fecha);

    boolean existsByFecha(LocalDate fecha);

}
