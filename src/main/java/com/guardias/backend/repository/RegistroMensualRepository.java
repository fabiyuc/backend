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

@Repository
public interface RegistroMensualRepository extends JpaRepository<RegistroMensual, Long> {

        Optional<RegistroMensual> findById(Long id);

        Optional<RegistroMensual> findByIdAndActivoTrue(Long id);

        @Query("SELECT DISTINCT rm FROM registrosMensuales rm JOIN rm.registroActividad ra WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector")
        List<RegistroMensual> findByAnioMesEfector(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector);


        Optional<RegistroMensual> findByAsistencialIdAndEfectorIdAndMesAndAnio(Long asistencialId, Long efectorId,
                        MesesEnum mes, int anio);


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
                        + "WHERE rm.anio = :anio AND rm.mes = :mes AND rm.efector.id = :idEfector "
                        + "AND ra.servicio.id = :idServicio")
        List<RegistroMensual> findByAnioMesEfectorServicio(
                        @Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("idServicio") Long idServicio);

        @Query("SELECT r.id FROM registrosMensuales r WHERE r.id IN :ids")
        List<Long> findExistingIds(@Param("ids") List<Long> ids);


        @Query("SELECT sh.montoTotal FROM registrosMensuales rm " +
                        "JOIN rm.totalHoras sh " +
                        "WHERE rm.activo = true " +
                        "AND rm.asistencial.id = :asistencialId " +
                        "AND rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND sh.activo = true")
        BigDecimal findMontoTotalHorasByFiltrosSinQuincena(
                        @Param("asistencialId") Long asistencialId,
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio);

        // @Query("SELECT sh.montoTotal FROM registrosMensuales rm " +
        @Query("SELECT COALESCE(SUM(sh.montoTotal), 0) FROM registrosMensuales rm " +
                        "JOIN rm.totalHoras sh " +
                        "WHERE rm.activo = true " +
                        "AND rm.asistencial.id = :asistencialId " +
                        "AND rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.estadoFacturacion IN :estados " +
                        "AND sh.activo = true")
        BigDecimal findMontoTotalByFiltros(
                        @Param("asistencialId") Long asistencialId,
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estados") List<EstadoFacturacionEnum> estados);

        @Query("SELECT rm.totalHoras.montoTotal FROM registrosMensuales rm " +
                        "WHERE rm.id = :id " +
                        "AND rm.activo = true")
        Optional<BigDecimal> findMontoTotalById(@Param("id") Long id);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosIncompletos(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
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
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosCompletos(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estado") EstadoFacturacionEnum estado);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosRegularizados(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estado") EstadoFacturacionEnum estado);

        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.estadoFacturacion = :estado " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosByEfectorAndMesAndAnioAndEstado(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estado") EstadoFacturacionEnum estado);

        /**
         * Busca registros mensuales por efector, asistencial, mes y año
         * Incluye TODAS las quincenas del mes
         */
        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.asistencial.id = :asistencialId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND rm.activo = true")
        List<RegistroMensual> findByEfectorAndAsistencialAndMesAndAnio(
                        @Param("efectorId") Long efectorId,
                        @Param("asistencialId") Long asistencialId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio);

       
        /**
         * Busca registros pendientes (estado NULL o PENDIENTE)
         */
        @Query("SELECT rm FROM registrosMensuales rm " +
                        "WHERE rm.efector.id = :efectorId " +
                        "AND rm.asistencial.id = :asistencialId " +
                        "AND rm.mes = :mes " +
                        "AND rm.anio = :anio " +
                        "AND (rm.estadoFacturacion IS NULL OR rm.estadoFacturacion = :estadoFacturacion) " +
                        "AND rm.activo = true")
        List<RegistroMensual> findRegistrosPendientes(
                        @Param("efectorId") Long efectorId,
                        @Param("asistencialId") Long asistencialId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("estadoFacturacion") EstadoFacturacionEnum estadoFacturacion);

}
