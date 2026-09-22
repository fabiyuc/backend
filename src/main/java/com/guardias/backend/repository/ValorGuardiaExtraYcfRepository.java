package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.FamiliaValorBaseEnum;

@Repository
public interface ValorGuardiaExtraYcfRepository extends JpaRepository<ValorGuardiaExtrayCF, Long> {

    Optional<List<ValorGuardiaExtrayCF>> findByActivoTrue();

    Optional<ValorGuardiaExtrayCF> findById(Long id);

    boolean existsById(Long id);

    List<ValorGuardiaExtrayCF> findByActivo(boolean activo);

    // Busca valores específicos para un hospital
    Optional<ValorGuardiaExtrayCF> findByHospitalesIdAndActivoTrue(Long hospitalId);

    // Busca valores genéricos (sin hospitales asignados)
    Optional<ValorGuardiaExtrayCF> findByActivoTrueAndHospitalesIsEmpty();

    List<ValorGuardiaExtrayCF> findByFamiliaValorBaseAndNivelComplejidadAndActivoTrue(
            FamiliaValorBaseEnum familia,
            int nivelComplejidad);

    @Query("SELECT v FROM valoresGuardiasExtraYcf v " +
            "LEFT JOIN FETCH v.hospitales h " +
            "WHERE v.activo = true " +
            "AND v.fechaInicio <= :fecha " +
            "AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    List<ValorGuardiaExtrayCF> buscarVigentes(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasExtraYcf v WHERE v.esServicioCritico = true " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaExtrayCF> findServicioCriticoVigente(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasExtraYcf v JOIN v.hospitales h " +
            "WHERE h.id = :idHospital " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaExtrayCF> findEspecificoPorHospitalVigente(@Param("idHospital") Long idHospital,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasExtraYcf v WHERE v.hospitales IS EMPTY AND v.esServicioCritico = false " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaExtrayCF> findGenericoVigente(@Param("fecha") LocalDate fecha);
}
