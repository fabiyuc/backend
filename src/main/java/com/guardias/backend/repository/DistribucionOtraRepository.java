package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.DistribucionOtra;

@Repository
public interface DistribucionOtraRepository extends JpaRepository<DistribucionOtra, Long> {

        Optional<List<DistribucionOtra>> findByActivoTrue();

        Optional<DistribucionOtra> findById(Long id);

        List<DistribucionOtra> findByFechaInicio(LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesOtras dg WHERE dg.activo = :activo AND dg.persona.id = :personaId AND dg.fechaInicio = :fechaInicio")
        List<DistribucionOtra> findByActivoAndPersonaIdAndFechaInicio(@Param("activo") boolean activo,
                        @Param("personaId") Long personaId, @Param("fechaInicio") LocalDate fechaInicio);

        @Query("SELECT dg FROM distribucionesOtras dg WHERE dg.persona.id = :personaId")
        Optional<List<DistribucionOtra>> findByPersonaId(@Param("personaId") Long personaId);

        @Query("SELECT dg FROM distribucionesOtras dg WHERE dg.efector.id = :efectorId")
        Optional<List<DistribucionOtra>> findByEfectorId(@Param("efectorId") Long efectorId);

        boolean existsById(Long id);

        boolean existsByEfectorId(Long efectorId);

        boolean existsByPersonaId(Long personaId);

        List<DistribucionOtra> findByActivo(boolean activo);

        @Query("SELECT dg FROM distribucionesOtras dg WHERE dg.activo = true AND dg.persona.id = :idPersona " +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        List<DistribucionOtra> findByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query("SELECT COUNT(dg) > 0 FROM distribucionesOtras dg WHERE dg.activo = true AND dg.persona.id = :idPersona "
                        +
                        "AND FUNCTION('MONTH', dg.fechaInicio) = :mes " +
                        "AND FUNCTION('YEAR', dg.fechaInicio) = :anio")
        boolean existsByActivoPersonaAndFechaInicio(
                        @Param("idPersona") Long idPersona,
                        @Param("mes") int mes,
                        @Param("anio") int anio);
}
