package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionConsultorio;
import com.guardias.backend.enums.DiasEnum;

@Repository
public interface DistribucionConsultorioRepository extends JpaRepository<DistribucionConsultorio, Long> {

        Optional<List<DistribucionConsultorio>> findByActivoTrue();

        List<DistribucionConsultorio> findByFechaInicio(LocalDate fechaInicio);

        @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.persona.id = :personaId")
        Optional<List<DistribucionConsultorio>> findByPersonaId(@Param("personaId") Long personaId);

        @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.activo = :activo AND dc.persona.id = :personaId AND dc.fechaInicio = :fechaInicio")
        List<DistribucionConsultorio> findByActivoAndPersonaIdAndFechaInicio(@Param("activo") boolean activo,
                        @Param("personaId") Long personaId, @Param("fechaInicio") LocalDate fechaInicio);

        @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.activo = :activo AND dc.persona.id = :personaId AND dc.fechaInicio = :fechaInicio AND dc.fechaFinalizacion = :fechaFinalizacion")
        List<DistribucionConsultorio> findByActivoAndPersonaIdAndFechaInicioAndFechaFin(@Param("activo") boolean activo,
                        @Param("personaId") Long personaId, @Param("fechaInicio") LocalDate fechaInicio,
                        @Param("fechaFinalizacion") LocalDate fechaFinalizacion);

        @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.efector.id = :efectorId")
        Optional<List<DistribucionConsultorio>> findByEfectorId(@Param("efectorId") Long efectorId);

        boolean existsById(Long id);

        boolean existsByEfectorId(Long efectorId);

        boolean existsByPersonaId(Long personaId);

        List<DistribucionConsultorio> findByActivo(boolean activo);

        @Query("""
                        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END
                        FROM distribucionesConsultorios d
                        WHERE d.dia = :dia
                        AND :fecha BETWEEN d.fechaInicio AND d.fechaFinalizacion
                        AND d.persona.id = :idAsistencial
                        AND d.efector.id = :idEfector
                        AND d.activo = true
                        """)
        boolean existsByDiaAndFechaAndPersonaAndEfector(
                        @Param("dia") DiasEnum dia,
                        @Param("fecha") LocalDate fecha,
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector);

        @Query(nativeQuery = true, value = """
                        SELECT *
                        FROM distribuciones_consultorios d
                        WHERE d.id_persona = :idAsistencial
                        AND d.id_efector = :idEfector
                        AND :fechaIngreso BETWEEN d.fecha_inicio AND d.fecha_finalizacion
                        AND d.activo = 1
                        """)
        List<DistribucionConsultorio> findValidDistribuciones(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("fechaIngreso") LocalDate fechaInicio);
        /* @Query(nativeQuery = true, value = """
                        SELECT *
                        FROM distribuciones_consultorios d
                        WHERE d.id_persona = :idAsistencial
                        AND d.id_efector = :idEfector
                        AND :fechaIngreso BETWEEN d.fecha_inicio AND d.fecha_finalizacion
                        AND CAST(:horaIngreso AS TIME) = CAST(d.hora_ingreso AS TIME)
                        AND CAST(:horaEgreso AS TIME) = DATEADD(HOUR, d.cantidad_horas, CAST(d.hora_ingreso AS TIME))
                        AND d.activo = 1
                        """)
        Optional<DistribucionConsultorio> findValidDistribucion(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("fechaIngreso") LocalDate fechaInicio,
                        @Param("horaIngreso") String horaIngreso,
                        @Param("horaEgreso") String horaEgreso); */

        @Query("SELECT dc FROM distribucionesConsultorios dc WHERE dc.activo = true AND dc.persona.id = :idPersona " +
                        "AND FUNCTION('MONTH', dc.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dc.fechaInicio) = :anio")
        List<DistribucionConsultorio> findByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query("SELECT COUNT(dc) > 0 FROM distribucionesConsultorios dc WHERE dc.activo = true AND dc.persona.id = :idPersona "
                        +
                        "AND FUNCTION('MONTH', dc.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dc.fechaInicio) = :anio")
        boolean existsByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        List<DistribucionConsultorio> findByPersonaIdAndActivoTrue(Long idPersona);

        boolean existsByPersonaIdAndActivoTrue(Long idPersona);
}
