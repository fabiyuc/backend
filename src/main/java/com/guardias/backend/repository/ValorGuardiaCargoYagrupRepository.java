package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.enums.FamiliaValorBaseEnum;

@Repository
public interface ValorGuardiaCargoYagrupRepository extends JpaRepository<ValorGuardiaCargoYagrup, Long> {

    Optional<List<ValorGuardiaCargoYagrup>> findByActivoTrue();

    Optional<ValorGuardiaCargoYagrup> findById(Long id);

    boolean existsById(Long id);

    List<ValorGuardiaCargoYagrup> findByActivo(boolean activo);

    // Busca valores específicos para un hospital
    Optional<ValorGuardiaCargoYagrup> findByHospitalesIdAndActivoTrue(Long hospitalId);

    // Busca valores genéricos (sin hospitales asignados)
    Optional<ValorGuardiaCargoYagrup> findByActivoTrueAndHospitalesIsEmpty();

    List<ValorGuardiaCargoYagrup> findByFamiliaValorBaseAndNivelComplejidadAndActivoTrue(
            FamiliaValorBaseEnum familia,
            int nivelComplejidad);

    @Query("SELECT v FROM valoresGuardiasCargosYagrup v " +
            "LEFT JOIN FETCH v.hospitales h " +
            "WHERE v.activo = true " +
            "AND v.fechaInicio <= :fecha " +
            "AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    List<ValorGuardiaCargoYagrup> buscarVigentes(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasCargosYagrup v WHERE v.esServicioCritico = true " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaCargoYagrup> findServicioCriticoVigente(@Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasCargosYagrup v JOIN v.hospitales h " +
            "WHERE h.id = :idHospital " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaCargoYagrup> findEspecificoPorHospitalVigente(@Param("idHospital") Long idHospital,
            @Param("fecha") LocalDate fecha);

    @Query("SELECT v FROM valoresGuardiasCargosYagrup v WHERE v.nivelComplejidad = :nivel " +
            "AND v.hospitales IS EMPTY AND v.esServicioCritico = false " +
            "AND v.fechaInicio <= :fecha AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    Optional<ValorGuardiaCargoYagrup> findGenericoPorNivelVigente(@Param("nivel") int nivel,
            @Param("fecha") LocalDate fecha);
}
