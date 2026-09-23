package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.BonoUti;

@Repository
public interface BonoUtiRepository extends JpaRepository<BonoUti, Long> {
    
    Optional<List<BonoUti>> findByActivoTrue();

    Optional<BonoUti> findById(Long id);

    boolean existsById(Long id);

    @Query("SELECT b FROM BonosUti b WHERE b.activo = true AND b.fechaInicio <= :fecha AND (b.fechaFin IS NULL OR b.fechaFin >= :fecha)")
    Optional<BonoUti> obtenerVigente(@Param("fecha") LocalDate fecha);

}
