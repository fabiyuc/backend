package com.guardias.backend.repository;

import java.math.BigDecimal;
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
            SELECT d.id
            FROM distribuciones_consultorios d
            WHERE d.id_persona = :idPersona
            AND d.id_efector = :idEfector
            AND d.fecha_inicio = :fechaInicio
            AND d.fecha_finalizacion = :fechaFinalizacion
            AND CAST(d.hora_ingreso AS TIME) = CAST(:horaIngreso AS TIME)
            AND d.cantidad_horas = :cantidadHoras
            AND d.activo = 1
            """)
    Long findIdByDistribucionConsultorioDto(
            @Param("idPersona") Long idPersona,
            @Param("idEfector") Long idEfector,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFinalizacion") LocalDate fechaFinalizacion,
            @Param("horaIngreso") String horaIngreso,
            @Param("cantidadHoras") BigDecimal cantidadHoras);

}
