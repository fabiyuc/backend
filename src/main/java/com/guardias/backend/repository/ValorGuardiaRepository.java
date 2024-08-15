package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardia;

@Repository
public interface ValorGuardiaRepository extends JpaRepository<ValorGuardia, Long>{

    Optional<List<ValorGuardia>> findByActivoTrue();

    Optional<ValorGuardia> findById(Long id);

    boolean existsById(Long id);
 
   /*  @Query("SELECT v FROM ValorGuardia v WHERE v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<List<ValorGuardia>> getByFecha(@Param("fecha") LocalDate fecha); */
}
