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

  @Query("SELECT ct FROM cronogramasTentativos ct " +
      "WHERE ct.efector.id = :efectorId " +
      "AND ct.servicio.id = :idServicio " +
      "AND ct.activo = true")
  Optional<List<CronogramaTentativo>> findByEfectorAndServicio(@Param("efectorId") Long efectorId,
      @Param("idServicio") Long idServicio);

  boolean existsById(Long id);

  boolean existsByEfectorId(Long efectorId);

  Optional<CronogramaTentativo> findById(Long id);

  @Query(value = """
      SELECT CAST(CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS BIT)
      FROM cronogramas_tentativos c
      WHERE c.id_asistencial = :idAsistencial
        AND c.id_efector = :idEfector

        AND c.activo = 1
        AND (
          DATETIMEFROMPARTS(YEAR(c.fecha_ingreso), MONTH(c.fecha_ingreso), DAY(c.fecha_ingreso),
                            DATEPART(HOUR, c.hora_ingreso), DATEPART(MINUTE, c.hora_ingreso), 0, 0)
          <
          DATETIMEFROMPARTS(YEAR(:fechaEgreso), MONTH(:fechaEgreso), DAY(:fechaEgreso),
                            DATEPART(HOUR, :horaEgreso), DATEPART(MINUTE, :horaEgreso), 0, 0)
          AND
          DATETIMEFROMPARTS(YEAR(c.fecha_egreso), MONTH(c.fecha_egreso), DAY(c.fecha_egreso),
                            DATEPART(HOUR, c.hora_egreso), DATEPART(MINUTE, c.hora_egreso), 0, 0)
          >
          DATETIMEFROMPARTS(YEAR(:fechaIngreso), MONTH(:fechaIngreso), DAY(:fechaIngreso),
                            DATEPART(HOUR, :horaIngreso), DATEPART(MINUTE, :horaIngreso), 0, 0)
        )
      """, nativeQuery = true)
  boolean existsByCronogramaTentativo(
      @Param("fechaIngreso") LocalDate fechaIngreso,
      @Param("fechaEgreso") LocalDate fechaEgreso,
      @Param("horaIngreso") LocalTime horaIngreso,
      @Param("horaEgreso") LocalTime horaEgreso,
      @Param("idAsistencial") Long idAsistencial,
      @Param("idEfector") Long idEfector);

  @Query(value = """
          SELECT DISTINCT c.id_efector
          FROM cronogramas_tentativos c
          WHERE c.id_asistencial = :idAsistencial
            AND c.activo = 1
            AND (
              DATETIMEFROMPARTS(YEAR(c.fecha_ingreso), MONTH(c.fecha_ingreso), DAY(c.fecha_ingreso),
                                DATEPART(HOUR, c.hora_ingreso), DATEPART(MINUTE, c.hora_ingreso), 0, 0)
              <
              DATETIMEFROMPARTS(YEAR(:fechaEgreso), MONTH(:fechaEgreso), DAY(:fechaEgreso),
                                DATEPART(HOUR, :horaEgreso), DATEPART(MINUTE, :horaEgreso), 0, 0)
              AND
              DATETIMEFROMPARTS(YEAR(c.fecha_egreso), MONTH(c.fecha_egreso), DAY(c.fecha_egreso),
                                DATEPART(HOUR, c.hora_egreso), DATEPART(MINUTE, c.hora_egreso), 0, 0)
              >
              DATETIMEFROMPARTS(YEAR(:fechaIngreso), MONTH(:fechaIngreso), DAY(:fechaIngreso),
                                DATEPART(HOUR, :horaIngreso), DATEPART(MINUTE, :horaIngreso), 0, 0)
            )
      """, nativeQuery = true)
  List<Long> findEfectoresConCronogramaSuperpuesto(
      @Param("fechaIngreso") LocalDate fechaIngreso,
      @Param("fechaEgreso") LocalDate fechaEgreso,
      @Param("horaIngreso") LocalTime horaIngreso,
      @Param("horaEgreso") LocalTime horaEgreso,
      @Param("idAsistencial") Long idAsistencial);

  Optional<List<CronogramaTentativo>> findByEfectorIdAndActivoTrueAndAutorizadoFalse(Long idEfector);

  @Query(value = """
    SELECT ct.* FROM cronogramas_tentativos ct
    WHERE ct.id_asistencial = :idAsistencial
    AND ct.id_efector = :idEfector
    AND ct.id_tipo_guardia = :idTipoGuardia
    AND ct.id_servicio = :idServicio
    AND ct.fecha_ingreso = :fechaIngreso
    AND ABS(DATEDIFF(MINUTE, ct.hora_ingreso, CAST(:horaIngreso AS TIME))) <= 15
    AND ct.activo = 1
    """, nativeQuery = true)
  List<CronogramaTentativo> findCronogramaParaRegistro(
      @Param("idAsistencial") Long idAsistencial,
      @Param("idEfector") Long idEfector,
      @Param("idTipoGuardia") Long idTipoGuardia,
      @Param("idServicio") Long idServicio,
      @Param("fechaIngreso") LocalDate fechaIngreso,
      @Param("horaIngreso") LocalTime horaIngreso);

}