package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionGuardia;
import com.guardias.backend.enums.DiasEnum;

@Repository
public interface DistribucionGuardiaRepository extends JpaRepository<DistribucionGuardia, Long> {

        Optional<List<DistribucionGuardia>> findByActivoTrue();

        Optional<DistribucionGuardia> findById(Long id);

        List<DistribucionGuardia> findByFechaInicio(LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.activo = :activo AND dg.persona.id = :personaId AND dg.fechaInicio = :fechaInicio")
        List<DistribucionGuardia> findByActivoAndPersonaIdAndFechaInicio(@Param("activo") boolean activo,
                        @Param("personaId") Long personaId, @Param("fechaInicio") LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.persona.id = :personaId")
        Optional<List<DistribucionGuardia>> findByPersonaId(@Param("personaId") Long personaId);

        @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.efector.id = :efectorId")
        Optional<List<DistribucionGuardia>> findByEfectorId(@Param("efectorId") Long efectorId);

        boolean existsById(Long id);

        boolean existsByEfectorId(Long efectorId);

        boolean existsByPersonaId(Long personaId);

        List<DistribucionGuardia> findByActivo(boolean activo);

        @Query("""
                        SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM distribucionesGuardias d WHERE d.dia = :dia AND :fecha BETWEEN d.fechaInicio AND d.fechaFinalizacion  AND d.horaIngreso = :horaIngreso AND d.persona.id = :idAsistencial AND d.efector.id = :idEfector AND d.activo = true """)
        boolean existsByDiaAndFechaAndIdPersonaAndIdEfector(
                        @Param("dia") DiasEnum dia,
                        @Param("fecha") LocalDate fecha,
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector);

        @Query(nativeQuery = true, value = """
                        SELECT *
                        FROM distribuciones_guardias d
                        WHERE d.id_persona = :idAsistencial
                        AND d.id_efector = :idEfector
                        AND d.tipo_guardia = :tipoGuardia
                        AND :fechaIngreso BETWEEN d.fecha_inicio AND d.fecha_finalizacion
                        AND CAST(:horaIngreso AS TIME) = CAST(d.hora_ingreso AS TIME)
                        AND CAST(:horaEgreso AS TIME) = DATEADD(HOUR, d.cantidad_horas, CAST(d.hora_ingreso AS TIME))
                        AND d.activo = 1
                        """)
        Optional<DistribucionGuardia> findValidDistribucion(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("tipoGuardia") String tipoGuardia,
                        @Param("fechaIngreso") LocalDate fechaInicio,
                        @Param("horaIngreso") String horaIngreso,
                        @Param("horaEgreso") String horaEgreso);

        @Query("SELECT dg FROM distribucionesGuardias dg WHERE dg.activo = true AND dg.persona.id = :idPersona " +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        List<DistribucionGuardia> findByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query("SELECT COUNT(dg) > 0 FROM distribucionesGuardias dg WHERE dg.activo = true AND dg.persona.id = :idPersona "
                        +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        boolean existsByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        List<DistribucionGuardia> findByPersonaIdAndEfectorIdAndActivoTrue(Long idPersona, Long idEfector);
        
        boolean existsByPersonaIdAndEfectorIdAndActivoTrue(Long idPersona, Long idEfector);
}
