package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroActividad;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface RegistroActividadRepository extends JpaRepository<RegistroActividad, Long> {

    Optional<List<RegistroActividad>> findByActivoTrue();

    Optional<RegistroActividad> findById(Long id);

    boolean existsById(Long id);

    List<RegistroActividad> findByActivo(boolean activo);

    @Query("SELECT m FROM registrosActividades m " +
            "WHERE m.efector.id = :idEfector " +
            "AND MONTH(m.fechaIngreso) = :mes " +
            "AND YEAR(m.fechaIngreso) = :anio " +
            "AND m.activo = true")
    List<RegistroActividad> findMotivosByEfectorServicioMesAnio(@Param("idEfector") Long idEfector,
            @Param("mes") int mes,
            @Param("anio") int anio);


        @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END " +
           "FROM registrosActividades r " +
           "WHERE r.efector.id = :idEfector " +
            "AND MONTH(m.fechaIngreso) = :mes " +
            "AND YEAR(m.fechaIngreso) = :anio " +
           "AND r.activo = true " +
           "AND r.tipoGuardia.nombre = :tipoGuardia")
        boolean existsByEfectorAndMesAndAnioAndTipoGuardia(
        @Param("idEfector") Long idEfector,
        @Param("mes") int mes,
        @Param("anio") int anio,
        @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia
    );

}
