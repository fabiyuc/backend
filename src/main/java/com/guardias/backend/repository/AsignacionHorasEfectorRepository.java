package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.guardias.backend.entity.AsignacionHorasEfector;

public interface AsignacionHorasEfectorRepository extends JpaRepository<AsignacionHorasEfector, Long> {
    
    List<AsignacionHorasEfector> findByLegajoIdAndActivoTrue(Long idLegajo);

    // ¿Ya existe una asignación de ESTE efector que se solape con el rango recibido?
    @Query("SELECT a FROM asignacionesHorasEfector a " +
           "WHERE a.legajo.id = :idLegajo AND a.efector.id = :idEfector AND a.activo = true " +
           "AND a.fechaInicio <= :fechaFin AND a.fechaFinalizacion >= :fechaInicio")
    List<AsignacionHorasEfector> findSolapadasMismoEfector(@Param("idLegajo") Long idLegajo,
                                                            @Param("idEfector") Long idEfector,
                                                              @Param("fechaInicio") LocalDate fechaInicio,
                                                              @Param("fechaFin") LocalDate fechaFin);

    // Asignaciones activas de CUALQUIER efector del legajo que se solapen con el rango recibido
    // (para sumar cuánto ya está repartido en ese mes, entre todos los hospitales)
    @Query("SELECT a FROM asignacionesHorasEfector a " +
           "WHERE a.legajo.id = :idLegajo AND a.activo = true " +
           "AND a.fechaInicio <= :fechaFin AND a.fechaFinalizacion >= :fechaInicio")
    List<AsignacionHorasEfector> findActivasEnRango(@Param("idLegajo") Long idLegajo,
                                                       @Param("fechaInicio") LocalDate fechaInicio,
                                                       @Param("fechaFin") LocalDate fechaFin);
}
