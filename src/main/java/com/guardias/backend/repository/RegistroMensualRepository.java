package com.guardias.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.RegistroMensual;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

        Optional<RegistroMensual> findById(Long id);

        Optional<RegistroMensual> findByIdAndActivoTrue(Long id);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector")
        List<RegistroMensual> findByAnioMesEfector(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector AND rm.quincena = :quincena")
        List<RegistroMensual> findByAnioMesEfectorAndQuincena(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("quincena") QuincenaEnum quincena);

        Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
                        MesesEnum mes, int anio);

        Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnioAndQuincena(Long asistencialId,
                        Long efectorId,
                        MesesEnum mes, int anio, QuincenaEnum quincena);

        Optional<List<RegistroMensual>> findByActivoTrue();

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        boolean existsByAsistencialId(Long asistencialId);

        List<RegistroMensual> findByAnioAndMes(int anio, MesesEnum mes);

        boolean existsById(Long id);

        List<RegistroMensual> findByActivo(boolean activo);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra "
                        + "WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector "
                        + "AND ra.servicio.id = :idServicio")
        List<RegistroMensual> findByAnioMesEfectorAndServicio(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("idServicio") Long idServicio);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra "
                        + "WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector AND rm.quincena = :quincena "
                        + "AND ra.servicio.id = :idServicio")
        List<RegistroMensual> findByAnioMesEfectorServicioAndQuincena(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("idServicio") Long idServicio,
                        @Param("quincena") QuincenaEnum quincena);

        @Query("SELECT r.id FROM registrosMensuales r WHERE r.id IN :ids")
        List<Long> findExistingIds(@Param("ids") List<Long> ids);

        @Query("SELECT sh.montoTotal FROM registrosMensuales rm " +
                        "JOIN rm.totalHoras sh " +
                        "WHERE rm.activo = true " +
                        "AND rm.asistencial.id = :asistencialId " +
                        "AND rm.efector.id = :efectorId " +
                        "AND rm.quincena = :quincena " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND sh.activo = true")
        BigDecimal findMontoTotalHorasByFiltros(
                        @Param("asistencialId") Long asistencialId,
                        @Param("efectorId") Long efectorId,
                        @Param("quincena") QuincenaEnum quincena,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio);

        @Query("SELECT rm.totalHoras.montoTotal FROM registrosMensuales rm " +
                        "WHERE rm.id = :id " +
                        "AND rm.activo = true")
        Optional<BigDecimal> findMontoTotalById(@Param("id") Long id);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.quincena = :quincena " +
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosIncompletos(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("quincena") QuincenaEnum quincena,
                        @Param("estado") EstadoFacturacionEnum estado);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.estadoFacturacion IN :estados " + 
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosFueraDeTermino(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estados") List<EstadoFacturacionEnum> estados);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.quincena = :quincena " +
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosCompletos(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("quincena") QuincenaEnum quincena,
                        @Param("estado") EstadoFacturacionEnum estado);

}
