package com.guardias.backend.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Efector;
import com.guardias.backend.entity.RegistrosPendientes;

@Repository
public interface RegistrosPendientesRepository extends JpaRepository<RegistrosPendientes, Long> {

        List<RegistrosPendientes> findByActivoTrue();

        List<RegistrosPendientes> findByFecha(LocalDate fecha);

        List<RegistrosPendientes> findByEfector(@Param("efector") Efector efector);

        Optional<RegistrosPendientes> findByEfectorAndFecha(@Param("efector") Efector efector,
                        @Param("fecha") LocalDate fecha);

        @Query(value = "SELECT * FROM registros_pendientes WHERE id_efector = :idEfector AND MONTH(fecha) = :mes AND YEAR(fecha) = :anio", nativeQuery = true)
        List<RegistrosPendientes> findByEfectorAndMonthYear(@Param("idEfector") Long idEfector, @Param("mes") int mes,
                        @Param("anio") int anio);

        @Query(value = "SELECT rp.* FROM registros_pendientes rp JOIN registros_actividades ra ON rp.id = ra.id_registros_pendientes WHERE rp.id_efector = :idEfector AND MONTH(rp.fecha) = :mes AND YEAR(rp.fecha) = :anio AND ra.id_asistencial = :idAsistencial", nativeQuery = true)
        RegistrosPendientes findByEfectorMonthYearAndAsistencial(
                        @Param("idEfector") Long idEfector,
                        @Param("mes") int mes,
                        @Param("anio") int anio,
                        @Param("idAsistencial") Long idAsistencial);

        @Query("SELECT rp FROM registrosPendientes rp " +
                        "WHERE rp.efector.id = :idEfector " +
                        "AND MONTH(rp.fecha) = :mes " +
                        "AND YEAR(rp.fecha) = :anio " +
                        "AND rp.activo = true")
        List<RegistrosPendientes> findByEfectorIdAndFechaMonthAndFechaYear(
                        @Param("idEfector") Long idEfector,
                        @Param("mes") int mes,
                        @Param("anio") int anio);
                        
        @Query("SELECT rp FROM registrosPendientes rp " +
                        "WHERE rp.efector.id = :idEfector " +
                        "AND rp.activo = true")
        List<RegistrosPendientes> findByEfectorId(
                        @Param("idEfector") Long idEfector);

        Optional<RegistrosPendientes> findByEfectorIdAndActivoTrue(Long idEfector);

}
