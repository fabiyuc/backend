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
import com.guardias.backend.enums.AutorizadoTentativoEnum;

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

        @Query("SELECT ct FROM cronogramasTentativos ct WHERE ct.asistencial.id = :idAsistencial AND ct.activo = true")
        Optional<List<CronogramaTentativo>> findByIdAsistencial(@Param("idAsistencial") Long idAsistencial);

                        @Query(value = """
                            SELECT EXISTS(
                                SELECT 1
                                FROM cronogramas_tentativos c
                                WHERE c.id_asistencial = :idAsistencial
                                AND c.id_efector = :idEfector
                                AND c.activo = 1
                                AND c.autorizado != 'RECHAZADO'
                                AND TIMESTAMP(c.fecha_ingreso, c.hora_ingreso)
                                    < TIMESTAMP(:fechaEgreso, :horaEgreso)
                                AND TIMESTAMP(c.fecha_egreso, c.hora_egreso)
                                    > TIMESTAMP(:fechaIngreso, :horaIngreso)
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
                        AND ABS(DATEDIFF(MINUTE, ct.hora_ingreso, CAST(:horaIngreso AS TIME))) <= 60
                        AND ct.activo = 1
                        AND ct.autorizado = 'CONFIRMADO'
                        """, nativeQuery = true)
        List<CronogramaTentativo> findCronogramaParaRegistro(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("idTipoGuardia") Long idTipoGuardia,
                        @Param("idServicio") Long idServicio,
                        @Param("fechaIngreso") LocalDate fechaIngreso,
                        @Param("horaIngreso") LocalTime horaIngreso);

        @Query(value = """
                        SELECT ct.* FROM cronogramas_tentativos ct
                        WHERE ct.id_asistencial = :idAsistencial
                        AND ct.id_efector = :idEfector
                        AND ct.id_tipo_guardia = :idTipoGuardia
                        AND ct.id_servicio = :idServicio
                        AND ct.fecha_ingreso = :fechaIngreso
                        AND CAST(:horaIngreso AS TIME) >= ct.hora_ingreso  -- No antes de la hora programada
                        AND DATEDIFF(MINUTE, ct.hora_ingreso, CAST(:horaIngreso AS TIME)) <= 60  -- Máximo 60 min después
                        AND ct.activo = 1
                        AND ct.autorizado = 'CONFIRMADO'
                        """, nativeQuery = true)
        List<CronogramaTentativo> findCronogramaParaRegistroConRetraso(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("idTipoGuardia") Long idTipoGuardia,
                        @Param("idServicio") Long idServicio,
                        @Param("fechaIngreso") LocalDate fechaIngreso,
                        @Param("horaIngreso") LocalTime horaIngreso);

        @Query("SELECT ct FROM cronogramasTentativos ct WHERE ct.efector.id = :efectorId AND ct.activo = true AND ct.autorizado = :autorizado")
        Optional<List<CronogramaTentativo>> findByEfectorIdAndAutorizado(@Param("efectorId") Long efectorId,
                        @Param("autorizado") AutorizadoTentativoEnum autorizado);

        @Query("SELECT ct FROM cronogramasTentativos ct WHERE ct.efector.id = :efectorId  AND ct.autorizado = :autorizado")
        Optional<List<CronogramaTentativo>> findByEfectorIdAndAnulado(@Param("efectorId") Long efectorId,
                        @Param("autorizado") AutorizadoTentativoEnum autorizado);

        @Query("SELECT COUNT(ct) FROM cronogramasTentativos ct " +
                        "WHERE ct.efector.id = :idEfector " +
                        "AND ct.autorizado = :estado " +
                        "AND ct.activo = true")
        Long countByEfectorIdAndEstado(
                        @Param("idEfector") Long idEfector,
                        @Param("estado") AutorizadoTentativoEnum estado);

        @Query("""
                        SELECT COUNT(ct) > 0 FROM cronogramasTentativos ct
                        WHERE ct.fechaIngreso BETWEEN :fechaInicio AND :fechaFinalizacion
                        AND ct.activo = true
                        AND ct.asistencial.id = :idAsistencial
                        AND ct.efector.id = :idEfector
                        """)
        boolean existsByFechaIngresoBetweenAndActivoTrue(
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFinalizacion") LocalDate fechaFinalizacion,
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector);

        @Query("""
                        SELECT ct FROM cronogramasTentativos ct
                        WHERE ct.fechaIngreso BETWEEN :fechaInicio AND :fechaFinalizacion
                        AND ct.activo = true
                        AND ct.asistencial.id = :idAsistencial
                        AND ct.efector.id = :idEfector
                        """)
        List<CronogramaTentativo> findByFechaIngresoBetweenAndActivoTrue(
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFinalizacion") LocalDate fechaFinalizacion,
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector);

        @Query("""
                        SELECT ct FROM cronogramasTentativos ct
                        WHERE ct.fechaIngreso BETWEEN :fechaInicio AND :fechaFinalizacion
                        AND ct.asistencial.id = :idAsistencial
                        AND ct.efector.id = :idEfector
                        AND ct.autorizado = :autorizado
                        """)
        List<CronogramaTentativo> findByFechaIngresoBetweenAndAutorizado(
                        @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFinalizacion") LocalDate fechaFinalizacion,
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("autorizado") AutorizadoTentativoEnum autorizado);

        @Query(value = """
                        SELECT ct.* FROM cronogramas_tentativos ct
                        WHERE ct.id_asistencial = :idAsistencial
                        AND ct.id_efector = :idEfector
                        AND ct.fecha_ingreso = :fechaIngreso
                        AND CAST(:horaIngreso AS TIME) >= ct.hora_ingreso
                        AND DATEDIFF(MINUTE, ct.hora_ingreso, CAST(:horaIngreso AS TIME)) <= 120
                        """, nativeQuery = true)
        Optional<CronogramaTentativo> obtenerIdsCronograma(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("fechaIngreso") LocalDate fechaIngreso,
                        @Param("horaIngreso") LocalTime horaIngreso);
}