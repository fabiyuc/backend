package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.TipoGuardiaEnum;

@Repository
public interface DdjjRepository extends JpaRepository<Ddjj, Long> {

        List<Ddjj> findByActivoTrue();

        Optional<Ddjj> findById(Long id);

        Optional<Ddjj> findByIdAndActivoTrue(Long id);

        boolean existsById(Long id);

        boolean existsByAnioAndMes(int anio, MesesEnum mes);

        boolean existsByAnio(int anio);

        List<Ddjj> findByAnio(int anio);

        List<Ddjj> findByAnioAndMes(int anio, MesesEnum mes);

        List<Ddjj> findByEfectorIdAndMesAndAnio(Long efectorId, MesesEnum mes, int anio);

        List<Ddjj> findByEfectorIdAndEstadoDdjjDirectorDPHAndActivoTrue(Long idEfector,
                        EstadoDdjjEnum estadoDdjjDirectorDPH);

        List<Ddjj> findByEfectorIdAndEstadoDdjjDirectorAndActivoTrue(Long idEfector,
                        EstadoDdjjEnum estadoDdjjDirector);

        List<Ddjj> findByEfectorIdAndDirectorIdAndEstadoDdjjDirectorAndActivoTrue(
                        Long idEfector,
                        Long idDirector,
                        EstadoDdjjEnum estadoDdjjDirector);

        @Query("SELECT COUNT(d) > 0 FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "LEFT JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio " +
                        "AND d.mes = :mes " +
                        "AND d.efector.id = :idEfector " +
                        "AND d.activo = true " +
                        "AND ra.activo = true " +
                        "AND ra.tipoGuardia.id = :idTipoGuardia")
        boolean existsByAnioMesEfectorAndTipoGuardia(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("idTipoGuardia") Long idTipoGuardia);

        @Query("SELECT COUNT(d) > 0 FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio " +
                        "AND d.mes = :mes " +
                        "AND d.efector.id = :idEfector " +
                        "AND d.activo = true " +
                        "AND ra.activo = true " +
                        "AND UPPER(ra.tipoGuardia.nombre) IN ('CARGO', 'AGRUPACION')")
        boolean existsDdjjConTipoGuardiaCargoOAgrupacion(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector);

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "LEFT JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                        "AND (ra IS NULL OR ra.esGuardiaIncompleta IS NULL OR ra.esGuardiaIncompleta = false)")
        List<Ddjj> findByAnioMesEfector(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector);

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "LEFT JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                        "AND ra.servicio.id = :idServicio " +
                        "AND (ra IS NULL OR ra.esGuardiaIncompleta IS NULL OR ra.esGuardiaIncompleta = false)")
        List<Ddjj> findByEfectorIdAndMesAndAnioServicio(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("idServicio") Long idServicio);

        @Query("SELECT COUNT(d) FROM Ddjjs d WHERE " +
       "d.activo = true AND " +
       "d.mes = :mes AND " +
       "d.anio = :anio AND " +
       "d.efector.id = :idEfector AND " +
       "d.tipoGuardia.nombre = :tipoGuardia AND " +
       "d.estadoDdjjDirector = :estado")
        Long countActiveByMesAnioEfectorAndTipoGuardia(
                @Param("mes") MesesEnum mes,
                @Param("anio") int anio,
                @Param("idEfector") Long idEfector,
                @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                @Param("estado") EstadoDdjjEnum estado);
}
