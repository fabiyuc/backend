package com.guardias.backend.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Factura;
import com.guardias.backend.enums.QuincenaEnum;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long>{

    Optional<List<Factura>> findByActivoTrue();
    
    boolean existsByAsistencialId(Long personaId);

    Optional<Factura> findByAsistencialId(Long asistencialId);

    @Query("SELECT SUM(f.monto) FROM facturas f " +
           "JOIN f.registrosMensuales rm " +
           "WHERE f.activo = true " +
           "AND f.asistencial.id = :asistencialId " +
           "AND rm.efector.id = :efectorId " +
           "AND rm.quincena = :quincena")
    BigDecimal sumMontoByAsistencialEfectorAndQuincena(
        @Param("asistencialId") Long asistencialId,
        @Param("efectorId") Long efectorId,
        @Param("quincena") QuincenaEnum quincena);
}
