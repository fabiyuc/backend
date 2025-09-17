package com.guardias.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Factura;
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
            "AND rm.anio = :anio")
    BigDecimal sumMontoByAsistencialEfectorQuincenaMesAnio(
            @Param("asistencialId") Long asistencialId,
            @Param("efectorId") Long efectorId,
            @Param("quincena") QuincenaEnum quincena,
            @Param("mes") MesesEnum mes,
            @Param("anio") int anio);

    @Query("SELECT DISTINCT f FROM facturas f " +
            "JOIN f.registrosMensuales rm " +
            "WHERE f.activo = true " +
            "AND rm.efector.id = :idEfector " +
            "AND rm.anio = :anio " +
            "AND rm.mes = :mes " +
            "AND rm.quincena = :quincena")
    List<Factura> findByAnioMesQuincena(
            @Param("idEfector") int idEfector,
            @Param("anio") int anio,
            @Param("mes") MesesEnum mes,
            @Param("quincena") QuincenaEnum quincena);

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

}
