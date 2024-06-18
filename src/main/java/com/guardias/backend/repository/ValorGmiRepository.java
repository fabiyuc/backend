package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGmi;

@Repository
public interface ValorGmiRepository extends JpaRepository<ValorGmi, Long> {

    Optional<List<ValorGmi>> findByActivoTrue();

    Optional<ValorGmi> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fechaInicio,
            LocalDate fechaFin);

    @Query("SELECT v FROM ValoresGmi v WHERE v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGmi> findByFechaInRange(@Param("fecha") LocalDate fecha);

    Optional<ValorGmi> findById(Long id);

    boolean existsById(Long id);

    List<ValorGmi> findByActivo(boolean activo);

    // @Query("SELECT vg FROM ValorGmi vg WHERE vg.fechaInicio <= :fecha AND
    // vg.fechaFin >= :fecha")
    // List<ValorGmi> findByDate(@Param("fecha") LocalDate fecha);

}
