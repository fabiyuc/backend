package com.guardias.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Factura;
import com.guardias.backend.enums.EstadoFacturacionEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {

      Optional<List<Factura>> findByActivoTrue();

      boolean existsByAsistencialId(Long personaId);

      Optional<Factura> findByAsistencialId(Long asistencialId);

      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :asistencialId " +
                  "AND rm.efector.id = :efectorId " +
                  "AND rm.quincena = :quincena " +
                  "AND rm.mes = :mes " +
                  "AND rm.activo = true " +
                  "AND rm.anio = :anio")
      BigDecimal sumMontoByAsistencialEfectorQuincenaMesAnio(
                  @Param("asistencialId") Long asistencialId,
                  @Param("efectorId") Long efectorId,
                  @Param("quincena") QuincenaEnum quincena,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio);

      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :asistencialId " +
                  "AND rm.efector.id = :efectorId " +
                  "AND rm.mes = :mes " +
                  "AND rm.activo = true " +
                  "AND rm.anio = :anio")
      BigDecimal sumMontoByAsistencialEfectorMesAnio(
                  @Param("asistencialId") Long asistencialId,
                  @Param("efectorId") Long efectorId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio);
      
                  @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :asistencialId " +
                  "AND rm.efector.id = :efectorId " +
                  "AND rm.mes = :mes " +
                  "AND rm.estadoFacturacion = :estadoFacturacion " +
                  "AND rm.activo = true " +
                  "AND rm.anio = :anio")
      BigDecimal sumMontoByAsistencialEfectorMesAnioEstadoFacturacion(
                  @Param("asistencialId") Long asistencialId,
                  @Param("efectorId") Long efectorId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio,
                  @Param("estadoFacturacion") EstadoFacturacionEnum estadoFacturacion);

      @Query("SELECT DISTINCT f FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " +
                  "AND rm.activo = true " +
                  "AND rm.quincena = :quincena")
      List<Factura> findByAnioMesQuincena(
                  @Param("idEfector") int idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes,
                  @Param("quincena") QuincenaEnum quincena);
      
      @Query("SELECT DISTINCT f FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.activo = true " +
                  "AND rm.mes = :mes ")
      List<Factura> findByAnioMes(
                  @Param("idEfector") int idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes);
      
      @Query("SELECT DISTINCT f FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :idAsistencial " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.activo = true " +
                  "AND rm.mes = :mes ")
      List<Factura> findByAsistencialYfiltros(
                  @Param("idEfector") int idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes,
                  @Param("idAsistencial") int idAsistencial);

      @Query("SELECT DISTINCT f FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND rm.asistencial.id = :idAsistencial " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " +
                  "AND rm.quincena = :quincena")
      List<Factura> findByFiltros(
                  @Param("idAsistencial") Long idAsistencial,
                  @Param("idEfector") Long idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes,
                  @Param("quincena") QuincenaEnum quincena);

      @Query("SELECT COUNT(f) > 0 FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :idAsistencial " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " +
                  "AND rm.quincena = :quincena")
      boolean existsByAsistencialAndEfectorAndAnioMesQuincena(
                  @Param("idAsistencial") Long idAsistencial,
                  @Param("idEfector") Long idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes,
                  @Param("quincena") QuincenaEnum quincena);

      // Método para contar facturas por filtros
      @Query("SELECT COUNT(f) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "JOIN rm.efector e " +
                  "WHERE f.asistencial.id = :idAsistencial " +
                  "AND e.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " +
                  "AND rm.quincena = :quincena " +
                  "AND rm.activo = true " +
                  "AND f.activo = true")
      long countByAsistencialAndEfectorAndPeriodo(
                  @Param("idAsistencial") Long idAsistencial,
                  @Param("idEfector") Long idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes,
                  @Param("quincena") QuincenaEnum quincena);

      @Query("SELECT rm.id FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.id = :facturaId " +
                  "AND rm.activo = true")
      Optional<Long> findRegistroMensualActivoIdByFacturaId(@Param("facturaId") Long facturaId);

      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.quincena = :quincena " +
                  "AND rm.anio = :anio")
      BigDecimal sumMontoFacturasExistentes(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("quincena") QuincenaEnum quincena,
                  @Param("anio") int anio);

      /**
       * Suma los montos de TODAS las facturas por asistencial, efector, mes y año
       */
      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio")
      BigDecimal sumMontosFacturasPorMesAnio(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio);

      /**
       * Cuenta la cantidad de facturas existentes por mes y año
       */
      @Query("SELECT COUNT(DISTINCT f) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio")
      int countFacturasExistentes(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio);

      /**
       * Cuenta facturas por quincena específica (PRIMERA o SEGUNDA)
       */
      @Query("SELECT COUNT(DISTINCT f) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio " +
                  "AND rm.quincena = :quincena")
      int countFacturasPorQuincena(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio,
                  @Param("quincena") QuincenaEnum quincena);

      /**
       * Cuenta facturas para registros pendientes (FUERA_DE_TERMINO)
       */
      @Query("SELECT COUNT(DISTINCT f) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio " +
                  "AND (rm.estadoFacturacion IS NULL OR rm.estadoFacturacion = :estadoFacturacion)")
      int countFacturasParaRegistrosPendientes(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio,
                  @Param("estadoFacturacion") EstadoFacturacionEnum estadoFacturacion);

      /**
       * Suma montos de facturas por quincena específica
       */
      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio " +
                  "AND rm.quincena = :quincena")
      BigDecimal sumMontosFacturasPorQuincena(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio,
                  @Param("quincena") QuincenaEnum quincena);

      /**
       * Suma montos de facturas para registros pendientes
       */
      @Query("SELECT COALESCE(SUM(f.monto), 0) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE rm.efector.id = :efectorId " +
                  "AND rm.asistencial.id = :asistencialId " +
                  "AND f.activo = true " +
                  "AND rm.mes = :mes " +
                  "AND rm.anio = :anio " +
                  "AND rm.activo = true " +
                  "AND (rm.estadoFacturacion IS NULL OR rm.estadoFacturacion = :estadoFacturacion)")
      BigDecimal sumMontosFacturasParaRegistrosPendientes(
                  @Param("efectorId") Long efectorId,
                  @Param("asistencialId") Long asistencialId,
                  @Param("mes") MesesEnum mes,
                  @Param("anio") int anio,
                  @Param("estadoFacturacion") EstadoFacturacionEnum estadoFacturacion);


 @Query("SELECT COUNT(f) > 0 FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "WHERE f.activo = true " +
                  "AND f.asistencial.id = :idAsistencial " +
                  "AND rm.efector.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " )
      boolean existsByAsistencialAndEfectorAndAnioMes(
                  @Param("idAsistencial") Long idAsistencial,
                  @Param("idEfector") Long idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes);

// Método para contar facturas por filtros
      @Query("SELECT COUNT(f) FROM facturas f " +
                  "JOIN f.registrosMensuales rm " +
                  "JOIN rm.efector e " +
                  "WHERE f.asistencial.id = :idAsistencial " +
                  "AND e.id = :idEfector " +
                  "AND rm.anio = :anio " +
                  "AND rm.mes = :mes " +
                  "AND rm.activo = true " +
                  "AND f.activo = true")
      long countByAsistencialAndEfectorAndPeriodoSinQuincena(
                  @Param("idAsistencial") Long idAsistencial,
                  @Param("idEfector") Long idEfector,
                  @Param("anio") int anio,
                  @Param("mes") MesesEnum mes);
}
