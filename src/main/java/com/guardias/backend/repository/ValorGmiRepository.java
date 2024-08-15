package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGmi;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface ValorGmiRepository extends JpaRepository<ValorGmi, Long> {

    Optional<List<ValorGmi>> findByActivoTrue();

    Optional<List<ValorGmi>> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fechaInicio,
            LocalDate fechaFin);

    @Query("SELECT v FROM ValoresGmi v WHERE v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<List<ValorGmi>> getByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM ValoresGmi v WHERE v.tipoGuardia = :tipoGuardia AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGmi> getByFechaAndTipoGuardia(@Param("fecha") LocalDate fecha,
            @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia);

    // tipoGuardia

    Optional<ValorGmi> findById(Long id);

    boolean existsById(Long id);

    //List<ValorGmi> findByActivo(boolean activo);

}
