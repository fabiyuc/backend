package com.guardias.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.guardias.backend.entity.Ddjj;
import com.guardias.backend.enums.CondicionDdjjEnum;
import com.guardias.backend.enums.EstadoDdjjEnum;
import com.guardias.backend.enums.MesesEnum;
import com.guardias.backend.enums.QuincenaEnum;
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
                        "AND d.tipoGuardia.nombre IN :tiposGuardia " +
                        "AND (ra IS NULL OR ra.esGuardiaIncompleta IS NULL OR ra.esGuardiaIncompleta = false)")
        List<Ddjj> findByAnioMesEfectorAndTipoGuardia(@Param("anio") int anio,
                        @Param("mes") MesesEnum mes,
                        @Param("idEfector") Long idEfector,
                        @Param("tiposGuardia") List<TipoGuardiaEnum> tiposGuardia);
        
        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                "LEFT JOIN d.registrosMensuales rm " +
                "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                "AND d.tipoGuardia.nombre = :tipoGuardia " +
                "AND d.quincena = :quincena " +
                "AND d.activo = true ")
        List<Ddjj> findByAnioMesEfectorAndTipoGuardiaAndQuincena(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes,
                @Param("idEfector") Long idEfector,
                @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                @Param("quincena") QuincenaEnum quincena);

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                "LEFT JOIN d.registrosMensuales rm " +
                "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                "AND d.tipoGuardia.nombre = :tipoGuardia " +
                "AND d.condicionDdjj = :condicionDdjj " +
                "AND d.activo = true ")
        List<Ddjj> findByAnioMesEfectorAndTipoGuardiaAndCondicionDdjj(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes,
                @Param("idEfector") Long idEfector,
                @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                @Param("condicionDdjj") CondicionDdjjEnum condicionDdjj);

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                        "AND ra.servicio.id = :idServicio " +
                        "AND d.tipoGuardia.nombre IN :tiposGuardia " +
                        "AND ra.activo = true " +
                        "AND (ra.esGuardiaIncompleta IS NULL OR ra.esGuardiaIncompleta = false)")
        List<Ddjj> findByEfectorIdAndMesAndAnioServicioAndTipoGuardia(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes,
                @Param("idEfector") Long idEfector,
                @Param("idServicio") Long idServicio,
                @Param("tiposGuardia") List<TipoGuardiaEnum> tiposGuardia);

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                        "JOIN d.registrosMensuales rm " +
                        "JOIN rm.registroActividad ra " +
                        "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                        "AND ra.servicio.id = :idServicio " +
                        "AND d.tipoGuardia.nombre = :tipoGuardia " +
                        "AND d.quincena = :quincena " +
                        "AND d.activo = true " +
                        "AND (ra.esGuardiaIncompleta IS NULL OR ra.esGuardiaIncompleta = false)")
        List<Ddjj> findByEfectorIdAndMesAndAnioServicioAndTipoGuardiaAndQuincena(
                @Param("anio") int anio,
                @Param("mes") MesesEnum mes,
                @Param("idEfector") Long idEfector,
                @Param("idServicio") Long idServicio,
                @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                @Param("quincena") QuincenaEnum quincena);

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

        @Query("SELECT DISTINCT d FROM Ddjjs d " +
                        "WHERE d.anio = :anio AND d.mes = :mes AND d.efector.id = :idEfector " +
                        "AND d.activo = true ")
        List<Ddjj> findByEfectorAndMesAndAnio(@Param("idEfector") Long idEfector,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio);

        @Query("SELECT COUNT(d) > 0 FROM Ddjjs d WHERE " +
                        "d.efector.id = :idEfector AND " +
                        "d.mes = :mes AND " +
                        "d.anio = :anio AND " +
                        "d.tipoGuardia.nombre = :tipoGuardia AND " +
                        "d.estadoDdjjDirector = :estadoDirector ")
        boolean existsByEfectorIdAndMesAndAnioAndTipoGuardiaAndEstadoDdjjDirector(
                        @Param("idEfector") Long idEfector,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                        @Param("estadoDirector") EstadoDdjjEnum estadoDirector);

        @Query("SELECT d.id FROM Ddjjs d WHERE d.efector.id = :efectorId AND d.mes = :mes AND d.anio = :anio AND d.tipoGuardia.nombre = :tipoGuardia AND d.estadoDdjjDirector = :estado")
        Optional<Long> findIdByEfectorIdAndMesAndAnioAndTipoGuardiaAndEstadoDdjjDirector(
                        @Param("efectorId") Long efectorId,
                        @Param("mes") MesesEnum mes,
                        @Param("anio") int anio,
                        @Param("tipoGuardia") TipoGuardiaEnum tipoGuardia,
                        @Param("estado") EstadoDdjjEnum estado);

        @Query("UPDATE Ddjjs d SET d.estadoDdjjDirectorDPH = :nuevoEstado WHERE d.id IN :ids")
        int updateEstadoDdjjDirectorDPHByIds(
                        @Param("ids") List<Long> ids,
                        @Param("nuevoEstado") EstadoDdjjEnum nuevoEstado);

        boolean existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndActivoTrue(
                        int anio, MesesEnum mes, Long efectorId, Long tipoGuardiaId);

       
        boolean existsByAnioAndMesAndEfectorIdAndTipoGuardiaIdAndQuincenaAndActivoTrue(
        int anio, MesesEnum mes, Long efectorId, Long tipoGuardiaId, QuincenaEnum quincena);
}
