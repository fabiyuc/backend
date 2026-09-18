package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ConstanteMonetariaBase;
import com.guardias.backend.enums.FamiliaValorBaseEnum;

@Repository
public interface ConstanteMonetariaBaseRepository extends JpaRepository<ConstanteMonetariaBase, Long> {

    Optional<List<ConstanteMonetariaBase>> findByActivoTrue();

    Optional<List<ConstanteMonetariaBase>> findByFechaInicioLessThanEqualAndFechaFinGreaterThanEqual(LocalDate fechaInicio,
            LocalDate fechaFin);

    @Query("SELECT v FROM ConstantesMonetariasBase v WHERE v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<List<ConstanteMonetariaBase>> getByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM ConstantesMonetariasBase v WHERE v.familiaValorBase = :familia AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ConstanteMonetariaBase> getByFechaAndFamilia(@Param("fecha") LocalDate fecha,
            @Param("familia") FamiliaValorBaseEnum familia);

    Optional<ConstanteMonetariaBase> findById(Long id);

    boolean existsById(Long id);

}
