package com.guardias.backend.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.CronogramaTentativo;

@Repository
public interface CronogramaTentativoRepository extends JpaRepository<CronogramaTentativo, Long> {

    Optional<List<CronogramaTentativo>> findByActivoTrue();

    @Query("SELECT ct FROM cronogramasTentativos ct WHERE ct.efector.id = :efectorId AND ct.activo = true")
    Optional<List<CronogramaTentativo>> findByEfectorId(@Param("efectorId") Long efectorId);

    boolean existsById(Long id);

    boolean existsByEfectorId(Long efectorId);

    Optional<CronogramaTentativo> findById(Long id);

    @Query(nativeQuery = true, value = """
            SELECT CAST(CASE WHEN COUNT(c.id) > 0 THEN 1 ELSE 0 END AS BIT)
            FROM cronogramas_tentativos c
            WHERE c.fecha_ingreso = :fechaIngreso
            AND c.fecha_egreso = :fechaEgreso
            AND c.hora_ingreso = CAST(:horaIngreso AS TIME)
            AND c.hora_egreso = CAST(:horaEgreso AS TIME)
            AND c.id_asistencial = :idAsistencial
            AND c.id_efector = :idEfector
            AND c.id_tipo_guardia = :idTipoGuardia
            AND c.activo = 1
            """)
    boolean existsByCronogramaTentativo(
            @Param("fechaIngreso") LocalDate fechaIngreso,
            @Param("fechaEgreso") LocalDate fechaEgreso,
            @Param("horaIngreso") LocalTime horaIngreso,
            @Param("horaEgreso") LocalTime horaEgreso,
            @Param("idAsistencial") Long idAsistencial,
            @Param("idEfector") Long idEfector,
            @Param("idTipoGuardia") Long idTipoGuardia);
}
