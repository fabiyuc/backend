package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionGira;

@Repository
public interface DistribucionGiraRepository extends JpaRepository<DistribucionGira, Long> {

        Optional<DistribucionGira> findById(Long id);

        Optional<List<DistribucionGira>> findByActivoTrue();

        List<DistribucionGira> findByFechaInicio(LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.activo = :activo AND dg.persona.id = :personaId AND dg.fechaInicio = :fechaInicio")
        List<DistribucionGira> findByActivoAndPersonaIdAndFechaInicio(@Param("activo") boolean activo,
                        @Param("personaId") Long personaId, @Param("fechaInicio") LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.persona.id = :personaId")
        Optional<List<DistribucionGira>> findByPersonaId(@Param("personaId") Long personaId);

        @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.efector.id = :efectorId")
        Optional<List<DistribucionGira>> findByEfectorId(@Param("efectorId") Long efectorId);

        boolean existsById(Long id);

        boolean existsByEfectorId(Long efectorId);

        boolean existsByPersonaId(Long personaId);

        List<DistribucionGira> findByActivo(boolean activo);

        @Query("SELECT dg FROM distribucionesGiras dg WHERE dg.activo = true AND dg.persona.id = :idPersona " +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        List<DistribucionGira> findByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query("SELECT COUNT (dg) > 0 FROM distribucionesGiras dg WHERE dg.activo = true AND dg.persona.id = :idPersona "
                        +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        boolean existsByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query(nativeQuery = true, value = """
                        SELECT *
                        FROM distribuciones_giras d
                        WHERE d.id_persona = :idAsistencial
                        AND d.id_efector = :idEfector
                        AND :fechaIngreso BETWEEN d.fecha_inicio AND d.fecha_finalizacion
                        AND CAST(:horaIngreso AS TIME) = CAST(d.hora_ingreso AS TIME)
                        AND CAST(:horaEgreso AS TIME) = DATEADD(HOUR, d.cantidad_horas, CAST(d.hora_ingreso AS TIME))
                        AND d.activo = 1
                        """)
        Optional<DistribucionGira> findValidDistribucion(
                        @Param("idAsistencial") Long idAsistencial,
                        @Param("idEfector") Long idEfector,
                        @Param("fechaIngreso") LocalDate fechaInicio,
                        @Param("horaIngreso") String horaIngreso,
                        @Param("horaEgreso") String horaEgreso);


        List<DistribucionGira> findByPersonaIdAndActivoTrue(Long idPersona);
        
        boolean existsByPersonaIdAndActivoTrue(Long idPersona);
}
