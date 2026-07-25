package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.ValorGuardiaCargoYagrup;
import com.guardias.backend.entity.ValorGuardiaExtrayCF;
import com.guardias.backend.enums.TipoGuardiaEnum;

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

    List<ValorGuardiaExtrayCF> findByTipoGuardiaAndNivelComplejidadAndActivoTrue(
        TipoGuardiaEnum tipoGuardia, 
        int nivelComplejidad
    );

    @Query("SELECT v FROM valoresGuardiasExtraYcf v " + 
       "LEFT JOIN FETCH v.hospitales h " + 
       "WHERE v.activo = true " +
       "AND v.fechaInicio <= :fecha " +
       "AND (v.fechaFin IS NULL OR v.fechaFin >= :fecha)")
    List<ValorGuardiaExtrayCF> buscarVigentes(@Param("fecha") LocalDate fecha);
}
